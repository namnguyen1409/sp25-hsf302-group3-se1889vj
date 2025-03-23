package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterProduct(ProductFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

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
            if(filterDTO.getName() != null && !filterDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterDTO.getName().toLowerCase() + "%"));
            }
            if(filterDTO.getDescription() != null && !filterDTO.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filterDTO.getDescription().toLowerCase() + "%"));
            }
            if(filterDTO.getCategoryName() != null && !filterDTO.getCategoryName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), "%" + filterDTO.getCategoryName().toLowerCase() + "%"));
            }
            if (filterDTO.getBrandName()!= null && !filterDTO.getBrandName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")), "%" + filterDTO.getBrandName().toLowerCase() + "%"));
            }
            if(filterDTO.getPriceOriginFrom() != null && !filterDTO.getPriceOriginFrom().isEmpty()) {
                try {
                    BigDecimal priceOriginFrom = new BigDecimal(filterDTO.getPriceOriginFrom());
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("priceOrigin"), priceOriginFrom));
                } catch (NumberFormatException ignored) {}
            }
            if(filterDTO.getPriceOriginTo() != null && !filterDTO.getPriceOriginTo().isEmpty()) {
                try {
                    BigDecimal priceOriginTo = new BigDecimal(filterDTO.getPriceOriginTo());
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("priceOrigin"), priceOriginTo));
                } catch (NumberFormatException ignored) {}
            }

            if(filterDTO.getIsActive() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), filterDTO.getIsActive()));
            }


            if(filterDTO.getPriceSaleFrom() != null && !filterDTO.getPriceSaleFrom().isEmpty()) {
                try {
                    BigDecimal priceSaleFrom = new BigDecimal(filterDTO.getPriceSaleFrom());
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("priceSale"), priceSaleFrom));
                } catch (NumberFormatException ignored) {}
            }
            if(filterDTO.getPriceSaleTo() != null && !filterDTO.getPriceSaleTo().isEmpty()) {
                try {
                    BigDecimal priceSaleTo = new BigDecimal(filterDTO.getPriceSaleTo());
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("priceSale"), priceSaleTo));
                } catch (NumberFormatException ignored) {}
            }



            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
