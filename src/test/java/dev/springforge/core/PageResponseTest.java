package dev.springforge.core;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {
    @Test
    void copiesContentAndPreservesMetadata() {
        var source = new ArrayList<>(List.of("a", "b"));
        var page = new PageResponse<>(source, 0, 2, 2, 1, true);
        source.add("c");
        assertEquals(List.of("a", "b"), page.content());
        assertEquals(2, page.totalElements());
        assertTrue(page.last());
    }

    @Test
    void rejectsInvalidPageNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new PageResponse<>(List.of(), -1, 10, 0, 0, true));
    }
}
