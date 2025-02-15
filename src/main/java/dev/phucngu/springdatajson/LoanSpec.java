package dev.phucngu.springdatajson;

import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public interface LoanSpec {
    static Specification<Loan> hasAgeBetween(int min, int max) {
        return (root, query, builder) -> {
            Expression<Integer> age = builder.function(
                    "JSON_EXTRACT",
                    Integer.class,
                    root.get("metadata"),
                    builder.literal("$.age")

            );
            return builder.between(age, min, max);
        };
    }

    static Specification<Loan> hasNameLike(String name) {
        return (root, query, builder) -> {
            Expression<String> nameLike = builder.function(
                    "JSON_EXTRACT",
                    String.class,
                    root.get("metadata"),
                    builder.literal("$.name")
            );
            return builder.like(nameLike, "%" + name + "%");
        };
    }
}
