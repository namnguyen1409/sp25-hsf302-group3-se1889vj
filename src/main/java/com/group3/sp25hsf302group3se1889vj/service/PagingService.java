package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.filter.BaseFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagingService<T, F extends BaseFilterDTO> {
    Page<T> findAll(F filterDTO, Pageable pageable);
}
