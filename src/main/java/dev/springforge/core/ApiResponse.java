package dev.springforge.core;

/**
 * A small, consistent response envelope for REST APIs.
 *
 * @param success whether the operation succeeded
 * @param message human-readable status message
 * @param data response payload; null for error responses
 * @param <T> payload type
 */
public record ApiResponse<T>(boolean success, String message, T data) {
    public ApiResponse {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
