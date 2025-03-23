package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService extends PagingService<BrandDTO, BrandFilterDTO> {

    void save(BrandDTO brand);

    BrandDTO findById(Long id);

    void update(BrandDTO brand);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<BrandDTO> getBrands();

    void delete(Long id);
}