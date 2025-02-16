package com.devfiles.core.file.infrastructure.adapter.dto;

import com.devfiles.enterprise.infrastructure.adapter.database.Pagination;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetAllFilesRequestParams {
    @Min(value = 1, message = "Page must be greater than 0")
    private Integer page;

    public Integer getPage() {
        return page == null
                ? 1
                : page;
    }

    @Min(value = 1, message = "Limit must be greater than 0")
    @Max(value = 100, message = "Limit must be less than or equal to 100")
    private Integer limit = 10;

    public Integer getLimit() {
        return limit == null
                ? 10
                : limit;
    }

    private String search;

    public String getSearch() {
        return search == null
                ? ""
                : search;
    }

    @Pattern(regexp = "asc|desc", message = "Sort must be either asc or desc")
    private String sort;

    public String getSort() {
        return sort == null
                ? "desc"
                : sort;
    }

    @Pattern(regexp = "id|slug|name|size|mimeType|createdAt",
            message = "Sort by must be either id, slug, name, size, mimeType or createdAt")
    private String sortBy;

    public String getSortBy() {
        return sortBy == null
                ? "id"
                : sortBy;
    }

    public Pagination toPagination() {
        return Pagination.builder()
                .page(getPage())
                .limit(getLimit())
                .search(getSearch())
                .sort(getSort())
                .sortBy(getSortBy())
                .build();
    }
}
