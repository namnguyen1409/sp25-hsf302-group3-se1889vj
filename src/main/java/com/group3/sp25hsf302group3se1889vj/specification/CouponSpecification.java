package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Coupon;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CouponSpecification {
    public static Specification<Coupon> filterCoupon(CouponFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterDTO.getCode() != null && !filterDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + filterDTO.getCode().toLowerCase() + "%"));
            }
            if(filterDTO.getDescription() != null && !filterDTO.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterDTO.getDescription().toLowerCase() + "%"));
            }
            if(filterDTO.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filterDTO.getType()));
            }
            if(filterDTO.getValueFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("value"), filterDTO.getValueFrom()));
            }
            if(filterDTO.getValueTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("value"), filterDTO.getValueTo()));
            }
            if(filterDTO.getMinOrderValueFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minOrderValue"), filterDTO.getMinOrderValueFrom()));
            }
            if(filterDTO.getMinOrderValueTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("minOrderValue"), filterDTO.getMinOrderValueTo()));
            }
            if(filterDTO.getMaxDiscountFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxDiscount"), filterDTO.getMaxDiscountFrom()));
            }
            if(filterDTO.getMaxDiscountTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxDiscount"), filterDTO.getMaxDiscountTo()));
            }
            if(filterDTO.getMaxUsageFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxUsage"), filterDTO.getMaxUsageFrom()));
            }
            if(filterDTO.getMaxUsageTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxUsage"), filterDTO.getMaxUsageTo()));
            }
            if(filterDTO.getUsageCountFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("usageCount"), filterDTO.getUsageCountFrom()));
            }
            if(filterDTO.getUsageCountTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("usageCount"), filterDTO.getUsageCountTo()));
            }
            if(filterDTO.getMaxUsagePerUserFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxUsagePerUser"), filterDTO.getMaxUsagePerUserFrom()));
            }
            if(filterDTO.getMaxUsagePerUserTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxUsagePerUser"), filterDTO.getMaxUsagePerUserTo()));
            }
            if(filterDTO.getStartDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), filterDTO.getStartDateFrom()));
            }
            if(filterDTO.getStartDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), filterDTO.getStartDateTo()));
            }
            if(filterDTO.getEndDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), filterDTO.getEndDateFrom()));
            }
            if(filterDTO.getEndDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), filterDTO.getEndDateTo()));
            }
            if(filterDTO.getIsDeleted() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), filterDTO.getIsDeleted()));
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
