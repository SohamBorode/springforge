# SpringForge Core

SpringForge Core is an open-source Java library containing small, reusable building blocks for Spring-based REST APIs.

> **Project status:** Early development (0.1.0-SNAPSHOT). APIs may change. This project has not yet been published to Maven Central and has no external adoption claims.

## Current features

- `ApiResponse<T>`: consistent success and error response envelope.
- `PageResponse<T>`: framework-independent pagination response.
- `CorrelationIdFilter`: validates or generates a request correlation ID, returns it in `X-Correlation-ID`, and places it in SLF4J MDC for request-scoped logging.
- `GlobalExceptionHandler`: maps not-found, validation, and unexpected exceptions to consistent HTTP responses.
- `ResourceNotFoundException`: application exception for missing resources.
- `ResourceConflictException`: application exception for state conflicts such as duplicate resources.

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

## Example: global exception handling

Register `GlobalExceptionHandler` through component scanning. Throw `ResourceNotFoundException` when a requested resource does not exist:

```java
@GetMapping("/users/{id}")
public ApiResponse<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return ApiResponse.success(user);
}
```

The handler maps:
- `ResourceNotFoundException` to HTTP 404.
- `ResourceConflictException` to HTTP 409.
- `MethodArgumentNotValidException` to HTTP 400 with field-level messages.
- Other unhandled exceptions to HTTP 500 with a generic client-facing message. The original exception is logged server-side and is not exposed in the response.

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
