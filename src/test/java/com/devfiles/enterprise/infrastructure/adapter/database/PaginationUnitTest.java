package com.devfiles.enterprise.infrastructure.adapter.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginationUnitTest {
    @Test
    void shouldReturnZeroWhenPageIsLessThanOne() {
        var pagination = Pagination.builder()
                .page(0)
                .limit(10)
                .search("search")
                .sort("sort")
                .sortBy("sortBy")
                .build();

        var result = pagination.getPage();

        assertEquals(0, result);
    }

    @Test
    void shouldReturnZeroWhenPageIsOne() {
        var pagination = Pagination.builder()
                .page(1)
                .limit(10)
                .search("search")
                .sort("sort")
                .sortBy("sortBy")
                .build();

        var result = pagination.getPage();

        assertEquals(0, result);
    }

    @Test
    void shouldReturnEmptyStringWhenSearchIsNull() {
        var pagination = Pagination.builder()
                .page(1)
                .limit(10)
                .search(null)
                .sort("sort")
                .sortBy("sortBy")
                .build();

        var result = pagination.getSearch();

        assertEquals("", result);
    }

    @Test
    void shouldReturnSearchTrimmedAndLowerCased() {
        var pagination = Pagination.builder()
                .page(1)
                .limit(10)
                .search("  SEARCH  ")
                .sort("sort")
                .sortBy("sortBy")
                .build();

        var result = pagination.getSearch();

        assertEquals("search", result);
    }
}
