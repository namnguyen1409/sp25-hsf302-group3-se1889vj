package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.entity.Coupon;
import com.group3.sp25hsf302group3se1889vj.entity.UserCoupon;
import com.group3.sp25hsf302group3se1889vj.repository.CouponRepository;
import com.group3.sp25hsf302group3se1889vj.repository.UserCouponRepository;
import com.group3.sp25hsf302group3se1889vj.service.UserCouponService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Override
    public int countByCouponCodeAndCreatedBy(String code, String currentUsername) {
        return userCouponRepository.countByCouponCodeAndCreatedBy(code, currentUsername);
    }

    @Transactional
    @Override
    public void useCoupon(String code, String createdBy) {
        Coupon coupon = couponRepository.findByCode(code);
        coupon.setUsageCount(coupon.getUsageCount() + 1);
        couponRepository.save(coupon);
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCoupon(couponRepository.findByCode(code));
        userCoupon.setCreatedBy(createdBy);
        userCouponRepository.save(userCoupon);
    }
}
