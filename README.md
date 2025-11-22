# Control Plane BFF

Backend For Frontend (BFF) microservice for the real-time volatility & sentiment forecasting platform.

## Overview

This BFF service acts as an intermediary between the Control Plane UI and the internal `control-plane-service`. It:

- Exposes UI-oriented REST APIs under `/ui/control/**`
- Calls the internal `control-plane-service` via HTTP
- Aggregates and transforms domain DTOs into convenient view models for the frontend
- Provides endpoints for reading aggregated state and sending commands

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Web** (REST controllers)
- **Spring WebFlux** (WebClient for HTTP calls)
- **Spring Validation** (Bean Validation)
- **Spring Actuator** (Health checks)
- **Lombok** (Boilerplate reduction)
- **MapStruct** (DTO mapping)
- **Gradle** (Build tool)

## Project Structure

```
src/main/java/com/example/controlplanebff/
├── config/              # Configuration (WebClient, Actuator, etc.)
├── client/              # HTTP clients for calling control-plane-service
├── dto/
│   ├── domain/          # DTOs matching control-plane-service API
│   └── ui/              # UI view models for frontend
├── mapper/              # MapStruct mappers (domain ↔ UI DTOs)
├── service/             # Orchestration & aggregation logic
├── controller/          # REST controllers
├── exception/           # Error handling
└── validation/          # Custom validators
```

## Configuration

### Application Properties

Configure in `application.yml`:

```yaml
server:
  port: 8081

control-plane:
  base-url: http://control-plane-service:8080

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

## API Endpoints

### Read Operations

#### GET `/ui/control/streams/state`
Get aggregated streams state for the Control Plane dashboard.

**Query Parameters:**
- `assetType` (optional): Filter by asset type (e.g., "CRYPTO")
- `exchange` (optional): Filter by exchange code
- `marketType` (optional): Filter by market type

**Response:** `ControlPlaneStreamsStateResponse`

#### GET `/ui/control/markets/options`
Get market/asset options for UI selectors.

**Query Parameters:**
- `assetType` (optional): Filter by asset type

**Response:** `MarketsOptionsResponse`

### Command Operations

#### POST `/ui/control/streams/market/toggle`
Toggle market stream for a symbol.

**Request Body:** `ToggleMarketStreamRequest`

#### POST `/ui/control/streams/news/toggle`
Toggle news stream for a symbol.

**Request Body:** `ToggleNewsStreamRequest`

#### POST `/ui/control/forecast/config`
Upsert forecasting configuration.

**Request Body:** `UpsertForecastConfigUiRequest`

#### POST `/ui/control/backfill/jobs`
Create a backfill job.

**Request Body:** `CreateBackfillJobUiRequest`

## Building

```bash
./gradlew build
```

## Running

```bash
./gradlew bootRun
```

Or with a JAR:

```bash
java -jar build/libs/control-plane-bff-1.0.0-SNAPSHOT.jar
```

## Health Checks

- `/actuator/health` - Application health status
- `/actuator/info` - Application information

## Error Handling

The service includes a global exception handler that:

- Maps validation errors to HTTP 400 with detailed messages
- Maps downstream service errors (404 → 404, other 4xx/5xx → 502/500)
- Provides consistent error response format:

```json
{
  "timestamp": "2025-11-20T12:34:56Z",
  "path": "/ui/control/streams/market/toggle",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": ["baseAsset must not be blank"]
}
```

## Notes

- This BFF is **stateless** - no database, no Flyway
- All domain state is managed by `control-plane-service`
- No forecasting logic or streaming - only orchestration and data transformation
- No Spring Security configured at this stage



