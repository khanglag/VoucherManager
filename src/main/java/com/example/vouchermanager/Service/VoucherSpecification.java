package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.Entity.Voucher;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherSpecification {
    public static Specification<Voucher> filterVouchers(String title, BigDecimal discountValue, String status, LocalDate startDate, LocalDate endDate) {
        return (Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();  // Tạo điều kiện mặc định "true"

            if (title != null && !title.isEmpty()) {
                predicate = cb.and(predicate, cb.like(root.get("title"), "%" + title + "%"));
            }

            if (discountValue != null) {
                predicate = cb.and(predicate, cb.equal(root.get("discountValue"), discountValue));
            }

            if (status != null && !status.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (startDate != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startDate"), startDate));
            }

            if (endDate != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("endDate"), endDate));
            }

            return predicate;
        };
    }
}
