package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.StaticPageFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaticPageService {

    Page<StaticPageDTO> findAll(StaticPageFilterDTO filterDTO, Pageable pageable);

    void save(StaticPageDTO staticPageDTO);

    StaticPageDTO findById(Long id);

    void update(StaticPageDTO staticPageDTO);

    void delete(Long id);

    boolean existsBySlug(String slug);

    boolean existsByTitle(String title);
}