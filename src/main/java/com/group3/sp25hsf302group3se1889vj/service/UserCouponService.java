package com.group3.sp25hsf302group3se1889vj.service;

public interface UserCouponService {

    int countByCouponCodeAndCreatedBy(String code, String currentUsername);

    void useCoupon(String code, String createdBy);
}