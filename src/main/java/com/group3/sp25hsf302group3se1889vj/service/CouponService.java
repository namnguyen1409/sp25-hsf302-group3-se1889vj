package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {

    Page<CouponDTO> findAll(CouponFilterDTO filterDTO, Pageable pageable);

    void save(CouponDTO coupon);

    CouponDTO findById(Long id);

    void update(CouponDTO coupon);
}