package com.example.controlplanebff.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class WebSocketProxyService {

    private final WebSocketClient downstreamClient;
    private final ExecutorService executorService;
    private final ConcurrentMap<String, ClientProxyContext> activeProxies = new ConcurrentHashMap<>();

    public WebSocketProxyService() {
        this.downstreamClient = new ReactorNettyWebSocketClient();
        this.executorService = Executors.newCachedThreadPool();
    }

    public void proxy(WebSocketSession clientSession, URI downstreamUri) {
        log.info("Proxying WebSocket connection to downstream: {}", downstreamUri);
        
        String sessionId = clientSession.getId();
        BlockingQueue<String> clientMessageQueue = new LinkedBlockingQueue<>();
        AtomicBoolean clientClosed = new AtomicBoolean(false);
        AtomicBoolean downstreamClosed = new AtomicBoolean(false);
        
        // Store context for this proxy session
        ClientProxyContext context = new ClientProxyContext(
                clientSession,
                clientMessageQueue,
                clientClosed,
                downstreamClosed
        );
        activeProxies.put(sessionId, context);
        
        // Connect to downstream
        log.info("Attempting to connect to downstream WebSocket: {}", downstreamUri);
        downstreamClient.execute(
                downstreamUri,
                downstreamSession -> {
                    log.info("Downstream WebSocket connection established successfully: {}", downstreamUri);
                    context.setDownstreamSession(downstreamSession);
                    
                    // Bridge: downstream → client (reactive)
                    Mono<Void> downstreamToClient = downstreamSession.receive()
                            .map(WebSocketMessage::getPayloadAsText)
                            .doOnNext(message -> {
                                if (!clientClosed.get() && clientSession.isOpen()) {
                                    try {
                                        synchronized (clientSession) {
                                            if (clientSession.isOpen()) {
                                                clientSession.sendMessage(new TextMessage(message));
                                            }
                                        }
                                    } catch (Exception e) {
                                        log.error("Failed to send message from downstream to client", e);
                                        clientClosed.set(true);
                                        closeClientSession(clientSession);
                                    }
                                }
                            })
                            .doOnComplete(() -> {
                                log.info("Downstream connection completed: {}", downstreamUri);
                                downstreamClosed.set(true);
                                if (!clientClosed.get()) {
                                    closeClientSession(clientSession);
                                }
                            })
                            .doOnError(error -> {
                                log.error("Error receiving from downstream: {}", downstreamUri, error);
                                downstreamClosed.set(true);
                                if (!clientClosed.get()) {
                                    closeClientSession(clientSession, CloseStatus.SERVER_ERROR);
                                }
                            })
                            .then();
                    
                    // Bridge: client → downstream (reads from queue)
                    Mono<Void> clientToDownstream = Mono.fromRunnable(() -> {
                        try {
                            while (clientSession.isOpen() && !downstreamClosed.get() && !clientClosed.get()) {
                                try {
                                    // Blocking poll from queue with timeout
                                    String payload = clientMessageQueue.poll(1, TimeUnit.SECONDS);
                                    if (payload != null) {
                                        // Check for close signal
                                        if ("__CLOSE__".equals(payload)) {
                                            break;
                                        }
                                        if (downstreamSession.isOpen() && !downstreamClosed.get()) {
                                            downstreamSession.send(
                                                    Mono.just(downstreamSession.textMessage(payload))
                                            ).subscribe(
                                                    null,
                                                    error -> {
                                                        log.error("Failed to send message from client to downstream", error);
                                                        downstreamClosed.set(true);
                                                        if (!clientClosed.get()) {
                                                            closeClientSession(clientSession);
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    log.info("Client-to-downstream bridge interrupted");
                                    break;
                                } catch (Exception e) {
                                    log.error("Error in client-to-downstream bridge", e);
                                    break;
                                }
                            }
                            clientClosed.set(true);
                        } catch (Exception e) {
                            log.error("Error in client-to-downstream bridge", e);
                            clientClosed.set(true);
                        }
                    }).subscribeOn(Schedulers.fromExecutor(executorService))
                      .then();
                    
                    // When either side closes, clean up
                    return Mono.when(downstreamToClient, clientToDownstream)
                            .doFinally(signalType -> {
                                try {
                                    if (downstreamSession.isOpen()) {
                                        downstreamSession.close();
                                    }
                                } catch (Exception e) {
                                    log.debug("Error closing downstream session", e);
                                }
                                activeProxies.remove(sessionId);
                            })
                            .then();
                }
        ).doOnSubscribe(subscription -> {
            log.info("Subscribing to downstream WebSocket connection: {}", downstreamUri);
        }).doOnError(error -> {
            log.error("Failed to establish downstream WebSocket connection: {} - Error: {}", 
                    downstreamUri, error.getMessage(), error);
            if (!clientClosed.get() && clientSession.isOpen()) {
                log.warn("Closing client session due to downstream connection failure");
                closeClientSession(clientSession, CloseStatus.SERVER_ERROR);
            }
            activeProxies.remove(sessionId);
        }).doOnSuccess(result -> {
            log.info("Downstream WebSocket connection completed successfully: {}", downstreamUri);
        }).subscribe(
                null,
                error -> {
                    // Error handler - already handled in doOnError
                    log.error("Unexpected error in downstream WebSocket subscription", error);
                }
        );
    }
    
    public void handleClientMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        ClientProxyContext context = activeProxies.get(sessionId);
        if (context != null && !context.isClientClosed()) {
            try {
                context.getClientMessageQueue().offer(message.getPayload());
            } catch (Exception e) {
                log.error("Failed to queue client message", e);
            }
        }
    }
    
    public void handleClientClosed(WebSocketSession session) {
        String sessionId = session.getId();
        ClientProxyContext context = activeProxies.get(sessionId);
        if (context != null) {
            context.setClientClosed(true);
            // Signal the queue reader to stop
            context.getClientMessageQueue().offer("__CLOSE__");
            activeProxies.remove(sessionId);
        }
    }
    
    private static class ClientProxyContext {
        private final WebSocketSession clientSession;
        private final BlockingQueue<String> clientMessageQueue;
        private final AtomicBoolean clientClosed;
        private final AtomicBoolean downstreamClosed;
        private org.springframework.web.reactive.socket.WebSocketSession downstreamSession;
        
        public ClientProxyContext(
                WebSocketSession clientSession,
                BlockingQueue<String> clientMessageQueue,
                AtomicBoolean clientClosed,
                AtomicBoolean downstreamClosed) {
            this.clientSession = clientSession;
            this.clientMessageQueue = clientMessageQueue;
            this.clientClosed = clientClosed;
            this.downstreamClosed = downstreamClosed;
        }
        
        public WebSocketSession getClientSession() {
            return clientSession;
        }
        
        public BlockingQueue<String> getClientMessageQueue() {
            return clientMessageQueue;
        }
        
        public boolean isClientClosed() {
            return clientClosed.get();
        }
        
        public void setClientClosed(boolean closed) {
            this.clientClosed.set(closed);
        }
        
        public AtomicBoolean getDownstreamClosed() {
            return downstreamClosed;
        }
        
        public void setDownstreamSession(org.springframework.web.reactive.socket.WebSocketSession session) {
            this.downstreamSession = session;
        }
        
        public org.springframework.web.reactive.socket.WebSocketSession getDownstreamSession() {
            return downstreamSession;
        }
    }
    
    private void closeClientSession(WebSocketSession session) {
        closeClientSession(session, CloseStatus.NORMAL);
    }
    
    private void closeClientSession(WebSocketSession session, CloseStatus status) {
        try {
            if (session.isOpen()) {
                session.close(status);
            }
        } catch (Exception e) {
            log.debug("Error closing client session", e);
        }
    }
}

