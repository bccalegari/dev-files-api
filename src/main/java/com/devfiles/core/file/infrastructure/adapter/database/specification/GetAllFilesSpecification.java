package com.devfiles.core.file.infrastructure.adapter.database.specification;

import com.devfiles.core.file.infrastructure.adapter.database.entity.FileEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class GetAllFilesSpecification {
    public static Specification<FileEntity> withUserId(Long userId) {
        return (Root<FileEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<FileEntity> withDeletedAtNull() {
        return (Root<FileEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.isNull(root.get("deletedAt"));
    }

    public static Specification<FileEntity> withSearch(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }

        return (Root<FileEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            var slugPredicate = builder.like(
                    builder.lower(root.get("slug")), "%" + search + "%"
            );

            var namePredicate = builder.like(
                    builder.lower(root.get("name")), "%" + search + "%"
            );

            var mimeTypePredicate = builder.like(
                    builder.lower(root.get("mimeType")), "%" + search + "%"
            );

            var createdAtPredicate = builder.like(
                    builder.function("to_char", String.class, root.get("createdAt"), builder.literal("yyyy-MM-dd")),
                    "%" + search + "%"
            );

            if (isNumeric(search)) {
                var sizePredicate = builder.equal(root.get("size"), Long.parseLong(search));
                return builder.or(slugPredicate, namePredicate, mimeTypePredicate, sizePredicate, createdAtPredicate);
            }

            return builder.or(slugPredicate, namePredicate, mimeTypePredicate, createdAtPredicate);
        };
    }

    private static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
