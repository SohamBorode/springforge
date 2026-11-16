package dev.springforge.core.integration;

import dev.springforge.core.ResourceConflictException;
import dev.springforge.core.ResourceNotFoundException;
import dev.springforge.core.autoconfigure.SpringForgeAutoConfiguration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end MVC tests proving that SpringForge is discovered and active
 * in a normal Spring Boot servlet application.
 */
@SpringBootTest(classes = SpringForgeIntegrationTest.TestApplication.class)
@AutoConfigureMockMvc
class SpringForgeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void automaticallyAddsCorrelationIdToResponse() throws Exception {
        mockMvc.perform(get("/demo/ok"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Correlation-ID"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("ok"));
    }

    @Test
    void preservesValidIncomingCorrelationId() throws Exception {
        mockMvc.perform(get("/demo/ok").header("X-Correlation-ID", "client-request-123"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-ID", "client-request-123"));
    }

    @Test
    void automaticallyMapsNotFoundExceptionTo404() throws Exception {
        mockMvc.perform(get("/demo/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Demo resource not found"));
    }

    @Test
    void automaticallyMapsConflictExceptionTo409() throws Exception {
        mockMvc.perform(get("/demo/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Demo resource already exists"));
    }

    @Test
    void automaticallyMapsValidationFailureTo400() throws Exception {
        mockMvc.perform(post("/demo/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data.name").exists());
    }

    @Test
    void hidesUnexpectedExceptionDetailsFromClient() throws Exception {
        mockMvc.perform(get("/demo/failure"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }

    @SpringBootConfiguration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @Import(DemoController.class)
    static class TestApplication {
    }

    @RestController
    static class DemoController {

        @GetMapping("/demo/ok")
        DemoPayload ok() {
            return new DemoPayload(true, "ok");
        }

        @GetMapping("/demo/not-found")
        void notFound() {
            throw new ResourceNotFoundException("Demo resource not found");
        }

        @GetMapping("/demo/conflict")
        void conflict() {
            throw new ResourceConflictException("Demo resource already exists");
        }

        @GetMapping("/demo/failure")
        void failure() {
            throw new IllegalStateException("internal database detail");
        }

        @PostMapping("/demo/validate")
        DemoPayload validate(@Valid @RequestBody DemoRequest request) {
            return new DemoPayload(true, request.name());
        }
    }

    record DemoRequest(@NotBlank String name) {
    }

    record DemoPayload(boolean success, String data) {
    }
}
