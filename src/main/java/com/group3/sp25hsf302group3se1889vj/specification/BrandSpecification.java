package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Brand;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BrandSpecification {
    public static Specification<Brand> filterBrand(BrandFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.getName() != null && !filterDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDTO.getName().toLowerCase() + "%"));
            }
            if (filterDTO.getDescription() != null && !filterDTO.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterDTO.getDescription().toLowerCase() + "%"));
            }
            if (filterDTO.getLogo() != null && !filterDTO.getLogo().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("logo")), "%" + filterDTO.getLogo().toLowerCase() + "%"));
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
