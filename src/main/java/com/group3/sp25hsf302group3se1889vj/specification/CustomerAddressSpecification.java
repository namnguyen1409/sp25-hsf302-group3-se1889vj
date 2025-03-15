package com.group3.sp25hsf302group3se1889vj.specification;

import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerAddressSpecification {
    public static Specification<CustomerAddress> filterCustomerAddress(CustomerAddressFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // customer predicates
            if(filterDTO.getFirstname() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), "%" + filterDTO.getFirstname().toLowerCase() + "%"));
            }
            if(filterDTO.getLastname() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + filterDTO.getLastname().toLowerCase() + "%"));
            }
            if(filterDTO.getPhone() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + filterDTO.getPhone().toLowerCase() + "%"));
            }

            // address predicates
            if(filterDTO.getDistrict() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("district")), "%" + filterDTO.getDistrict().toLowerCase() + "%"));
            }
            if(filterDTO.getWard() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("ward")), "%" + filterDTO.getWard().toLowerCase() + "%"));
            }
            if(filterDTO.getAddress() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + filterDTO.getAddress().toLowerCase() + "%"));
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
