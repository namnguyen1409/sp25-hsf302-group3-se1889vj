package com.group3.sp25hsf302group3se1889vj.mapper;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Coupon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    private final ModelMapper modelMapper;

    public CouponDTO toDTO(Coupon entity) {
        return modelMapper.map(entity, CouponDTO.class);
    }

    public Coupon toEntity(CouponDTO dto) {
        return modelMapper.map(dto, Coupon.class);
    }
}
