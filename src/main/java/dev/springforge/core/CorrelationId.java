package dev.springforge.core;

import java.util.UUID;
import java.util.regex.Pattern;

/** Validation and generation helpers for request correlation IDs. */
public final class CorrelationId {
    private static final Pattern SAFE_ID = Pattern.compile("[A-Za-z0-9._-]{1,128}");

    private CorrelationId() {}

    /**
     * Returns the supplied ID when safe, otherwise generates a random UUID.
     * Restricting characters and length prevents newline/log injection.
     */
    public static String normalizeOrGenerate(String candidate) {
        if (candidate != null && SAFE_ID.matcher(candidate).matches()) {
            return candidate;
        }
        return UUID.randomUUID().toString();
    }
}
