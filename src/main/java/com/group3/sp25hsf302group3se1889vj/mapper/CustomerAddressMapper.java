package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import org.modelmapper.ModelMapper;

public class CustomerAddressMapper {
    ModelMapper modelMapper = new ModelMapper();

    public CustomerAddressDTO mapToCustomerAddressDTO(CustomerAddress customerAddress) {
        return modelMapper.map(customerAddress, CustomerAddressDTO.class);
    }

    public CustomerAddress mapToCustomerAddress(CustomerAddressDTO customerAddressDTO) {
        return modelMapper.map(customerAddressDTO, CustomerAddress.class);
    }
}
