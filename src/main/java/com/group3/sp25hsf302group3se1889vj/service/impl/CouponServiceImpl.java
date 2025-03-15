package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {
    @Override
    public Page<CouponDTO> findAll(CouponFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    @Override
    public void save(CouponDTO coupon) {

    }

    @Override
    public CouponDTO findById(Long id) {
        return null;
    }

    @Override
    public void update(CouponDTO coupon) {

    }
}
