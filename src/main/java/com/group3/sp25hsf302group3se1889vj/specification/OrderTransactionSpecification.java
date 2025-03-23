package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderTransactionFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.OrderTransaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderTransactionSpecification {
    public static Specification<OrderTransaction> filterOrderTransaction(OrderTransactionFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterDTO.getTransactionId() != null && !filterDTO.getTransactionId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("transactionId"), filterDTO.getTransactionId()));
            }

            if(filterDTO.getAmountFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filterDTO.getAmountFrom()));
            }
            if(filterDTO.getAmountTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filterDTO.getAmountTo()));
            }
            if (filterDTO.getVnPayResponseCode() != null && !filterDTO.getVnPayResponseCode().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("vnPayResponseCode"), filterDTO.getVnPayResponseCode()));
            }
            if (filterDTO.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filterDTO.getType()));
            }
            if (filterDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDTO.getStatus()));
            }

            // base predicates
            if(filterDTO.getCreatedAtFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filterDTO.getCreatedAtFrom()));
            }
            if(filterDTO.getCreatedAtTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filterDTO.getCreatedAtTo()));
            }
            if(filterDTO.getUpdatedAtFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), filterDTO.getUpdatedAtFrom()));
            }
            if(filterDTO.getUpdatedAtTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), filterDTO.getUpdatedAtTo()));
            }
            if(filterDTO.getCreatedBy() != null && !filterDTO.getCreatedBy().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("createdBy")), "%" + filterDTO.getCreatedBy().toLowerCase() + "%"));
            }
            if(filterDTO.getUpdatedBy() != null && !filterDTO.getUpdatedBy().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updatedBy")), "%" + filterDTO.getUpdatedBy().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
