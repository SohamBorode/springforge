package dev.springforge.core;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsResourceNotFoundTo404() {
        var response = handler.handleNotFound(
                new ResourceNotFoundException("User not found")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("User not found", response.getBody().message());
        assertNull(response.getBody().data());
    }

    @Test
    void mapsResourceConflictTo409() {
        var response = handler.handleConflict(
                new ResourceConflictException("Email already exists")
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("Email already exists", response.getBody().message());
        assertNull(response.getBody().data());
    }

    @Test
    void mapsValidationErrorsTo400WithFieldMessages() throws Exception {
        Object target = new Object();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "request");
        bindingResult.addError(new FieldError(
                "request", "email", "must be a valid email"
        ));

        Method method = TestController.class.getDeclaredMethod("submit", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(parameter, bindingResult);

        var response = handler.handleValidation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("Validation failed", response.getBody().message());
        assertEquals(
                Map.of("email", "must be a valid email"),
                response.getBody().data()
        );
    }

    @Test
    void mapsUnexpectedExceptionTo500WithoutExposingInternalMessage() {
        var response = handler.handleUnexpected(
                new IllegalStateException("database password is secret")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertNull(response.getBody().data());
    }

    private static class TestController {
        @SuppressWarnings("unused")
        void submit(String email) {
        }
    }
}
