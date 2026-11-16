package dev.springforge.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdTest {
    @Test
    void preservesSafeId() {
        assertEquals("request-123_A.b", CorrelationId.normalizeOrGenerate("request-123_A.b"));
    }

    @Test
    void generatesIdForNewlineInjection() {
        String generated = CorrelationId.normalizeOrGenerate("abc\nforged-log-entry");
        assertNotNull(generated);
        assertNotEquals("abc\nforged-log-entry", generated);
        assertTrue(generated.matches("[0-9a-f-]{36}"));
    }

    @Test
    void generatesIdForOverlongValue() {
        String generated = CorrelationId.normalizeOrGenerate("x".repeat(129));
        assertTrue(generated.matches("[0-9a-f-]{36}"));
    }
}
