package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.StaticPageFilterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StaticPageService extends PagingService<StaticPageDTO, StaticPageFilterDTO> {

    void save(StaticPageDTO staticPageDTO);

    StaticPageDTO findById(Long id);

    void update(StaticPageDTO staticPageDTO);

    void delete(Long id);

    boolean existsBySlug(String slug);

    boolean existsByTitle(String title);

    StaticPageDTO findBySlug(String slug);

    List<StaticPageDTO> getStaticPage();

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByTitleAndIdNot(String title, Long id);
}