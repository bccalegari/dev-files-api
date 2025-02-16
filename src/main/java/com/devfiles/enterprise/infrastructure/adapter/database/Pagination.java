package com.devfiles.enterprise.infrastructure.adapter.database;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Getter
public class Pagination {
    @Getter(AccessLevel.NONE)
    private final int page;
    private final int limit;
    @Getter(AccessLevel.NONE)
    private final String search;
    private final String sort;
    private final String sortBy;

    public int getPage() {
        return page < 1
                ? 0
                : (page - 1);
    }

    public String getSearch() {
        return search == null
                ? ""
                : search.trim().toLowerCase();
    }

    public Pageable toPageable() {
        return PageRequest.of(getPage(), limit, Sort.by(Sort.Direction.fromString(sort), sortBy));
    }
}
