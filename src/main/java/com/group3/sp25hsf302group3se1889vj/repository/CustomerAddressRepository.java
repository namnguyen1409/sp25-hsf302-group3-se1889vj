package com.group3.sp25hsf302group3se1889vj.repository;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long>, JpaSpecificationExecutor<CustomerAddress> {
    Page<CustomerAddress> findAll(Pageable pageable);
    boolean existsCustomerAddressByPhone(String phone);
    boolean existsCustomerAddressByPhoneAndIdNot(String phone, Long id);
    Optional<CustomerAddress> findById(Long id);

    List<CustomerAddress> findAllByCreatedBy(String currentUsername);

    boolean existsCustomerAddressByPhoneAndFullNameAndAddressAndCreatedBy(String phone,String fullName, String address, String createdBy);

    boolean existsCustomerAddressByPhoneAndFullNameAndAddressAndCreatedByAndIdNot(String phone, String fullName, String address, String createdBy, Long id);

    @Modifying
    @Query("update CustomerAddress c set c.isDefault = ?1 where c.createdBy = ?2")
    void setIsDefaultAddressByCreatedBy(boolean b, String createdBy);

    @Modifying
    @Query("update CustomerAddress c set c.isDefault = ?1 where c.id = ?2")
    void setIsDefaultAddressById(boolean b, Long id);
}
