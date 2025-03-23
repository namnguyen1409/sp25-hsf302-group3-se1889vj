package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Brand;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.BrandMapper;
import com.group3.sp25hsf302group3se1889vj.repository.BrandRepository;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.BrandSpecification;

import com.group3.sp25hsf302group3se1889vj.specification.CategorySpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final NotificationService notificationService;


    public Page<BrandDTO> findAll(BrandFilterDTO filterDTO, Pageable pageable) {
        Specification<Brand> specification = BrandSpecification.filterBrand(filterDTO);
        return brandRepository.findAll(specification, pageable)
                .map(brandMapper::toDTO);
    }

    @Override
    public void save(BrandDTO brand) {
        var entity = brandMapper.toEntity(brand);
        brandRepository.save(entity);
        sendBrandNotification("được tạo", brand, NotificationType.SUCCESS);
    }

    @Override
    public BrandDTO findById(Long id) {
        var entity = brandMapper.toDTO(brandRepository.findById(id).orElseThrow());
        return entity;
    }

    @Override
    public void update(BrandDTO brandDTO) {
        log.info("update brandDTO: {}", brandDTO);
        var brand = brandRepository.findById(brandDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        brand.setName(brandDTO.getName());
        brand.setLogo(brandDTO.getLogo());
        brand.setDescription(brandDTO.getDescription());
        brandRepository.save(brand);
        sendBrandNotification("được cập nhật", brandDTO, NotificationType.INFO);
    }

    @Override
    public boolean existsByName(String name) {
       return brandRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return brandRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public List<BrandDTO> getBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var brand = brandRepository.findById(id).orElseThrow();
        sendBrandNotification("bị xóa", brandMapper.toDTO(brand), NotificationType.ERROR);
        brandRepository.delete(brand);
    }

    /**
     * Tạo thông báo cho thao tác trên banner.
     */
    private void sendBrandNotification(String action, BrandDTO brand, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Nhãn hàng đã " + action);
        notification.setContent("Nhãn hàng \"" + brand.getName() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_BRAND);
    }

}
