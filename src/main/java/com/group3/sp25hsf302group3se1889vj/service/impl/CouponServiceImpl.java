package com.group3.sp25hsf302group3se1889vj.service.impl;


import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Coupon;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.CouponMapper;
import com.group3.sp25hsf302group3se1889vj.repository.CouponRepository;
import com.group3.sp25hsf302group3se1889vj.service.CouponService;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.CouponSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final NotificationService notificationService;

    @Override
    public Page<CouponDTO> findAll(CouponFilterDTO filterDTO, Pageable pageable) {
        Specification<Coupon> specification = CouponSpecification.filterCoupon(filterDTO);
        return couponRepository.findAll(specification, pageable)
                .map(couponMapper::toDTO);
    }

    @Override
    public void save(CouponDTO coupon) {
        couponRepository.save(couponMapper.toEntity(coupon));
    }

    @Override
    public CouponDTO findById(Long id) {
        return couponRepository.findById(id)
                .map(couponMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Transactional
    @Override
    public void update(CouponDTO coupon) {
        Coupon entity = couponRepository.findById(coupon.getId())
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        entity.setCode(coupon.getCode());
        entity.setDescription(coupon.getDescription());
        entity.setValue(coupon.getValue());
        entity.setMaxDiscount(coupon.getMaxDiscount());
        entity.setMinOrderValue(coupon.getMinOrderValue());
        entity.setMaxUsagePerUser(coupon.getMaxUsagePerUser());
        entity.setStartDate(coupon.getStartDate());
        entity.setEndDate(coupon.getEndDate());
        couponRepository.save(entity);
        sendCouponNotification("cập nhật", coupon, NotificationType.INFO);
    }

    @Override
    public void delete(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        sendCouponNotification("xóa", couponMapper.toDTO(coupon), NotificationType.WARNING);
        coupon.softDelete();
        couponRepository.save(coupon);
    }

    @Override
    public boolean isExistCode(String code) {
        return couponRepository.existsByCode(code);
    }

    @Override
    public boolean isExistCodeAndIdNot(String code, Long id) {
        return couponRepository.existsByCodeAndIdNot(code, id);
    }

    @Override
    public void restore(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.restore();
        couponRepository.save(coupon);
        sendCouponNotification("khôi phục", couponMapper.toDTO(coupon), NotificationType.INFO);

    }

    @Override
    public CouponDTO findByCode(String code) {
        return couponMapper.toDTO(couponRepository.findByCode(code));
    }


    private void sendCouponNotification(String action, CouponDTO couponDTO, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Mã giảm giá đã " + action);
        notification.setContent("Mã giảm giá \"" + couponDTO.getCode() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_COUPONS);
    }



}
