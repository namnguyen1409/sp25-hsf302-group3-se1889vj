package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductVariantFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.ProductVariant;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductVariantSpecification {
    public static Specification<ProductVariant> filterProductVariant(ProductVariantFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // custom predicates
            if(filterDTO.getProductId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("product").get("id"), filterDTO.getProductId()));
            }
            if(filterDTO.getProductSize() != null && !filterDTO.getProductSize().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("size")), "%" + filterDTO.getProductSize().toLowerCase() + "%"));
            }
            if(filterDTO.getProductColor() != null && !filterDTO.getProductColor().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("color")), "%" + filterDTO.getProductColor().toLowerCase() + "%"));
            }
            if(filterDTO.getQuantityFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), filterDTO.getQuantityFrom()));
            }
            if(filterDTO.getQuantityTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), filterDTO.getQuantityTo()));
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
