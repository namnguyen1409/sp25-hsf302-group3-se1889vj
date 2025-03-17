package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import com.group3.sp25hsf302group3se1889vj.mapper.CustomerAddressMapper;
import com.group3.sp25hsf302group3se1889vj.repository.CustomerAddressRepository;
import com.group3.sp25hsf302group3se1889vj.service.CustomerAddressService;
import com.group3.sp25hsf302group3se1889vj.specification.CustomerAddressSpecification;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {
    private final ResourcePatternResolver resourcePatternResolver;
    private CustomerAddressRepository customerAddressRepository;
    private CustomerAddressMapper customerAddressMapper;
    @Override
    public Page<CustomerAddressDTO> searchCustomerAddresses(CustomerAddressFilterDTO filter, Pageable pageable) {
        Specification<CustomerAddress> specification = CustomerAddressSpecification.filterCustomerAddress(filter);
        return customerAddressRepository.findAll(specification, pageable).map(customerAddressMapper::mapToCustomerAddressDTO);
    }

    @Override
    public void save(CustomerAddressDTO customerAddressDTO) {
        customerAddressRepository.save(customerAddressMapper.mapToCustomerAddress(customerAddressDTO));
    }

    @Override
    public boolean existsCustomerAddressByPhone(String phone) {
        return customerAddressRepository.existsCustomerAddressByPhone(phone);
    }

    @Override
    public boolean existsCustomerAddressByPhoneAndIdNot(String phone, Long id) {
        return customerAddressRepository.existsCustomerAddressByPhoneAndIdNot(phone, id);
    }

    @Override
    public CustomerAddressDTO findById(Long id) {
        Optional<CustomerAddress> customerAddress = customerAddressRepository.findById(id);
        if(customerAddress.isPresent()) {
            return customerAddressMapper.mapToCustomerAddressDTO(customerAddress.get());
        }
        return null;
    }

    @Override
    public List<CustomerAddressDTO> findAllByCreatedBy(String currentUsername) {
        return customerAddressRepository.findAllByCreatedBy(currentUsername).stream().map(customerAddressMapper::mapToCustomerAddressDTO).toList();
    }


}
