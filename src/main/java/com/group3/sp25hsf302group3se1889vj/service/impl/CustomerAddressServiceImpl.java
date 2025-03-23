package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.CustomerAddressDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerAddressFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.CustomerAddress;
import com.group3.sp25hsf302group3se1889vj.entity.Order;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.mapper.CustomerAddressMapper;
import com.group3.sp25hsf302group3se1889vj.repository.CustomerAddressRepository;
import com.group3.sp25hsf302group3se1889vj.service.CustomerAddressService;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.CustomerAddressSpecification;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerAddressServiceImpl implements CustomerAddressService {
    private final ResourcePatternResolver resourcePatternResolver;
    private final NotificationService notificationService;
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
        sendNotification("được thêm", customerAddressDTO, NotificationType.INFO);
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
        return customerAddress.map(address -> customerAddressMapper.mapToCustomerAddressDTO(address)).orElse(null);
    }

    @Override
    public List<CustomerAddressDTO> findAllByCreatedBy(String currentUsername) {
        return customerAddressRepository.findAllByCreatedBy(currentUsername).stream().map(customerAddressMapper::mapToCustomerAddressDTO).toList();
    }

    @Override
    public boolean isExistAddress(CustomerAddressDTO address) {
        return customerAddressRepository.existsCustomerAddressByPhoneAndFullNameAndAddressAndCreatedBy(address.getPhone(), address.getFullName(), address.getAddress(), address.getCreatedBy());
    }

    @Override
    public void update(CustomerAddressDTO address) {
        CustomerAddress customerAddress = customerAddressMapper.mapToCustomerAddress(address);
        customerAddressRepository.save(customerAddress);
        sendNotification("được cập nhật", address, NotificationType.INFO);
    }

    @Override
    public boolean isExistAddressAndIdNot(CustomerAddressDTO address, Long id) {
        return customerAddressRepository.existsCustomerAddressByPhoneAndFullNameAndAddressAndCreatedByAndIdNot(address.getPhone(), address.getFullName(), address.getAddress(), address.getCreatedBy(), id);
    }

    @Override
    public void delete(Long id) {
        Optional<CustomerAddress> customerAddress = customerAddressRepository.findById(id);
        customerAddress.ifPresent(address -> sendNotification("được xóa", customerAddressMapper.mapToCustomerAddressDTO(address), NotificationType.WARNING));
        customerAddressRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id, String createdBy) {
        customerAddressRepository.setIsDefaultAddressByCreatedBy(false, createdBy);
        customerAddressRepository.setIsDefaultAddressById(true, id);
    }

    private void sendNotification(String action, CustomerAddressDTO customerAddressDTO, NotificationType type) {
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Địa chỉ đã " + action);
        notification.setContent("Địa chỉ \"" + customerAddressDTO.getAddress() + "\" đã " + action);
        notificationService.sendNotificationToUser(notification, customerAddressDTO.getCreatedBy());

    }



}
