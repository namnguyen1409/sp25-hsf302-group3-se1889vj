package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.OrderFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> filterOrder(OrderFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDTO.getStatus()));
            }
            if (filterDTO.getTotalPriceFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), filterDTO.getTotalPriceFrom()));
            }
            if (filterDTO.getTotalPriceTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), filterDTO.getTotalPriceTo()));
            }
            if (filterDTO.getDiscountAmountFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountAmount"), filterDTO.getDiscountAmountFrom()));
            }
            if (filterDTO.getDiscountAmountTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("discountAmount"), filterDTO.getDiscountAmountTo()));
            }
            if (filterDTO.getFinalPriceFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("finalPrice"), filterDTO.getFinalPriceFrom()));
            }
            if (filterDTO.getFinalPriceTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("finalPrice"), filterDTO.getFinalPriceTo()));
            }

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
                predicates.add(criteriaBuilder.equal(root.get("createdBy"),  filterDTO.getCreatedBy()));
            }
            if(filterDTO.getUpdatedBy() != null && !filterDTO.getUpdatedBy().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updatedBy")), "%" + filterDTO.getUpdatedBy().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
