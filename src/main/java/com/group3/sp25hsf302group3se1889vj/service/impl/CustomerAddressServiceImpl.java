package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import com.group3.sp25hsf302group3se1889vj.mapper.CustomerAddressMapper;
import com.group3.sp25hsf302group3se1889vj.repository.CustomerAddressRepository;
import com.group3.sp25hsf302group3se1889vj.service.CustomerAddressService;
import com.group3.sp25hsf302group3se1889vj.specification.CustomerAddressSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {
    private CustomerAddressRepository customerAddressRepository;
    private CustomerAddressMapper customerAddressMapper;
    @Override
    public Page<CustomerAddressDTO> searchCustomerAddresses(CustomerAddressFilterDTO filter, Pageable pageable) {
        Specification<CustomerAddress> specification = CustomerAddressSpecification.filterCustomerAddress(filter);
        return customerAddressRepository.findAll(specification, pageable).map(customerAddressMapper::mapToCustomerAddressDTO);
    }
}
