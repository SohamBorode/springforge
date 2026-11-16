package dev.springforge.core;

import java.util.List;

/** Framework-independent pagination metadata and content. */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public PageResponse {
        content = List.copyOf(content);
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1) throw new IllegalArgumentException("size must be >= 1");
        if (totalElements < 0) throw new IllegalArgumentException("totalElements must be >= 0");
        if (totalPages < 0) throw new IllegalArgumentException("totalPages must be >= 0");
    }
}
