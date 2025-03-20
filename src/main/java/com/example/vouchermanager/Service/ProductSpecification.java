package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.Entity.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterProducts(String productName, BigDecimal minPrice, BigDecimal maxPrice, Boolean status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productName != null && !productName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + productName + "%"));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
