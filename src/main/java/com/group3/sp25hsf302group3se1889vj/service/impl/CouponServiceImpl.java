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
import com.group3.sp25hsf302group3se1889vj.specification.CouponSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

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

    @Override
    public void update(CouponDTO coupon) {

    }

    @Override
    public void delete(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
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
    }

}
