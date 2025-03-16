package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Banner;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.BannerMapper;
import com.group3.sp25hsf302group3se1889vj.repository.BannerRepository;
import com.group3.sp25hsf302group3se1889vj.service.BannerService;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.BannerSpecification;
import com.group3.sp25hsf302group3se1889vj.specification.CategorySpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {


    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final NotificationService notificationService;

    @Override
    public Page<BannerDTO> findAll(BannerFilterDTO filterDTO, Pageable pageable) {
        Specification<Banner> specification = BannerSpecification.filterBanner(filterDTO);
        return bannerRepository.findAll(specification, pageable)
                .map(bannerMapper::toDTO);
    }

    @Override
    public void save(BannerDTO banner) {
        bannerRepository.save(bannerMapper.toEntity(banner));
        sendBannerNotification("được tạo", banner, NotificationType.INFO);
    }

    @Override
    public BannerDTO findById(Long id) {
        return bannerRepository.findById(id)
                .map(bannerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    @Override
    public void update(BannerDTO banner) {
        Banner entity = bannerRepository.findById(banner.getId())
                .orElseThrow(() -> new RuntimeException("Banner not found"));
        entity.setTitle(banner.getTitle());
        entity.setUrl(banner.getUrl());
        entity.setImage(banner.getImage());
        entity.setDescription(banner.getDescription());
        bannerRepository.save(entity);
        sendBannerNotification("được cập nhật", banner, NotificationType.INFO);
    }

    @Override
    public void delete(Long id) {
        BannerDTO banner = findById(id);
        bannerRepository.deleteById(id);
        sendBannerNotification("bị xóa", banner, NotificationType.WARNING);
    }

    /**
     * Tạo thông báo cho thao tác trên banner.
     */
    private void sendBannerNotification(String action, BannerDTO banner, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Banner đã " + action);
        notification.setContent("Banner \"" + banner.getTitle() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_BANNERS);
    }

}
