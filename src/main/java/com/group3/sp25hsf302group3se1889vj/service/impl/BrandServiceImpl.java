package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Brand;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import com.group3.sp25hsf302group3se1889vj.mapper.BrandMapper;
import com.group3.sp25hsf302group3se1889vj.repository.BrandRepository;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
import com.group3.sp25hsf302group3se1889vj.specification.BrandSpecification;
import com.group3.sp25hsf302group3se1889vj.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public Page<BrandDTO> findAll(BrandFilterDTO brandFilterDTO, Pageable pageable) {
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
