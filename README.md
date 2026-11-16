# SpringForge Core

SpringForge Core is an open-source Java library containing small, reusable building blocks for Spring-based REST APIs.

> **Project status:** Early development (0.1.0-SNAPSHOT). APIs may change. This project has not yet been published to Maven Central and has no external adoption claims.

## Current features

- `ApiResponse<T>`: consistent success and error response envelope.
- `PageResponse<T>`: framework-independent pagination response.
- `CorrelationIdFilter`: validates or generates a request correlation ID, returns it in `X-Correlation-ID`, and places it in SLF4J MDC for request-scoped logging.

## Requirements

- Java 17+
- Maven 3.9+
- Spring Framework 6 / Spring Boot 3 applications

## Build and test

```bash
mvn clean verify
```

## Add to a Spring Boot application

Until a published release is available, clone this repository and install it locally:

```bash
mvn clean install
```

Then add the dependency to your application's `pom.xml`:

```xml
<dependency>
    <groupId>dev.springforge</groupId>
    <artifactId>springforge-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Example: API response

```java
@GetMapping("/health")
public ApiResponse<String> health() {
    return ApiResponse.success("Service is running");
}
```

Example JSON:

```json
{
  "success": true,
  "message": "OK",
  "data": "Service is running"
}
```

## Example: pagination response

```java
PageResponse<String> response = new PageResponse<>(
    List.of("item-1", "item-2"),
    0,
    20,
    2,
    1,
    true
);
```

## Example: correlation ID filter

Register the filter as a Spring bean:

```java
@Configuration
public class WebConfiguration {
    @Bean
    public CorrelationIdFilter correlationIdFilter() {
        return new CorrelationIdFilter();
    }
}
```

Every request receives a validated `X-Correlation-ID` response header. A supplied ID is accepted only if it matches `[A-Za-z0-9._-]{1,128}`; otherwise a UUID is generated. The filter removes the MDC value after the request completes.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md). Please open an issue before making a large change.

## Security

Do not put secrets, personal information, access tokens, or sensitive data in correlation IDs. See [SECURITY.md](SECURITY.md).

## License

MIT. See [LICENSE](LICENSE).
