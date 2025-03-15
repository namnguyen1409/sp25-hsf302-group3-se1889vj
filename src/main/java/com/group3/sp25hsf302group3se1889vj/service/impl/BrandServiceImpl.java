package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    @Override
    public Page<BrandDTO> findAll(BrandFilterDTO filterDTO, Pageable pageable) {
        return null;
    }

    @Override
    public void save(BrandDTO brand) {

    }

    @Override
    public BrandDTO findById(Long id) {
        return null;
    }

    @Override
    public void update(BrandDTO brand) {

    }
}
