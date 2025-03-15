package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    Page<BrandDTO> findAll(BrandFilterDTO filterDTO, Pageable pageable);

    void save(BrandDTO brand);

    BrandDTO findById(Long id);

    void update(BrandDTO brand);
}