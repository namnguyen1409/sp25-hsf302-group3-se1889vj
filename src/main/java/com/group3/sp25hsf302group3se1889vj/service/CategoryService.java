package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CategoryFilterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getParentCategories();

    Page<CategoryDTO> searchCategories(CategoryFilterDTO categoryFilterDTO, Pageable pageable);

    void save(CategoryDTO categoryDTO);

    boolean existsByNameAndParentId(String name, Long parentId);

    CategoryDTO findById(Long id);

    boolean existsByNameAndParentIdAndIdNot(String name, Long parentId, Long id);

    void update(CategoryDTO categoryDTO);

    void delete(Long id);
}