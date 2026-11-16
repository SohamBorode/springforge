package dev.springforge.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {
    @Test
    void createsSuccessfulResponse() {
        ApiResponse<String> response = ApiResponse.success("created", "hello");
        assertTrue(response.success());
        assertEquals("created", response.message());
        assertEquals("hello", response.data());
    }

    @Test
    void createsErrorResponseWithoutPayload() {
        ApiResponse<Void> response = ApiResponse.error("Not found");
        assertFalse(response.success());
        assertEquals("Not found", response.message());
        assertNull(response.data());
    }

    @Test
    void rejectsBlankMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> new ApiResponse<>(true, " ", "value"));
    }
}
