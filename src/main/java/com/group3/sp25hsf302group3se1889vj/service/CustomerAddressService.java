package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerAddressService {
    Page<CustomerAddressDTO> searchCustomerAddresses(CustomerAddressFilterDTO filter, Pageable pageable);
    void save(CustomerAddressDTO customerAddressDTO);
    boolean existsCustomerAddressByPhone(String phone);
    boolean existsCustomerAddressByPhoneAndIdNot(String phone, Long id);
    CustomerAddressDTO findById(Long id);

    List<CustomerAddressDTO> findAllByCreatedBy(String currentUsername);

    boolean isExistAddress(CustomerAddressDTO address);

    void update(CustomerAddressDTO address);

    boolean isExistAddressAndIdNot(CustomerAddressDTO address, Long id);

    void delete(Long id);

    void setDefaultAddress(Long id, String currentUsername);
}