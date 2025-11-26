package com.example.controlplanebff.websocket;

import com.example.controlplanebff.config.MarketLiveStreamGatewayProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProxyWebSocketHandler extends TextWebSocketHandler {

    private final MarketLiveStreamGatewayProperties properties;
    private final WebSocketProxyService proxyService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String streamId = extractStreamId(path);
        
        if (streamId == null || streamId.isEmpty()) {
            log.warn("Invalid streamId in path: {}", path);
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        
        log.info("WebSocket connection established: sessionId={}, streamId={}, path={}", 
                session.getId(), streamId, path);
        
        URI targetUri = URI.create(properties.wsBaseUrl() + "/" + streamId);
        log.info("Target downstream URI: {}", targetUri);
        
        try {
            proxyService.proxy(session, targetUri);
        } catch (Exception e) {
            log.error("Error initiating proxy for session {}: {}", session.getId(), e.getMessage(), e);
            if (session.isOpen()) {
                session.close(CloseStatus.SERVER_ERROR);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Forward client message to proxy service
        proxyService.handleClientMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: sessionId={}, status={}", session.getId(), status);
        proxyService.handleClientClosed(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session {}", session.getId(), exception);
        proxyService.handleClientClosed(session);
    }

    private String extractStreamId(String path) {
        if (path == null) {
            return null;
        }
        
        // Expected format: /ws/cp/v1/streams/{streamId}
        String[] parts = path.split("/");
        
        // Find "streams" and get the next part as streamId
        for (int i = 0; i < parts.length - 1; i++) {
            if ("streams".equals(parts[i]) && i + 1 < parts.length) {
                String streamId = parts[i + 1];
                if (streamId != null && !streamId.isEmpty()) {
                    return streamId;
                }
            }
        }
        
        return null;
    }
}

