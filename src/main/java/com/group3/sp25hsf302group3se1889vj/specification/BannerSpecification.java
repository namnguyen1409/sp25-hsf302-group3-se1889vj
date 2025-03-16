package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Banner;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BannerSpecification {
    public static Specification<Banner> filterBanner(BannerFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterDTO.getTitle() != null && !filterDTO.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filterDTO.getTitle().toLowerCase() + "%"));
            }

            if(filterDTO.getUrl() != null && !filterDTO.getUrl().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("url")), "%" + filterDTO.getUrl().toLowerCase() + "%"));
            }

            if(filterDTO.getImage() != null && !filterDTO.getImage().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("image")), "%" + filterDTO.getImage().toLowerCase() + "%"));
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
