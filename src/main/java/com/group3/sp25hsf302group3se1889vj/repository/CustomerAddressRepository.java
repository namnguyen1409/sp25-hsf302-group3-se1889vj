package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {
    Page<CustomerAddress> findAll(Pageable pageable);
}
