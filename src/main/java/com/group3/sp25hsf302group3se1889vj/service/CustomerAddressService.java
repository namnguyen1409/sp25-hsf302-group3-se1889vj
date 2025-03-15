package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerAddressService {
    Page<CustomerAddressDTO> searchCustomerAddresses(CustomerAddressFilterDTO filter, Pageable pageable);
    void save(CustomerAddressDTO customerAddressDTO);
    boolean existsCustomerAddressByPhone(String phone);
    boolean existsCustomerAddressByPhoneAndIdNot(String phone, Long id);
    CustomerAddressDTO findById(Long id);
}