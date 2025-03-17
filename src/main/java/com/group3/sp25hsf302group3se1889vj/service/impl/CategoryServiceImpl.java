package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CategoryFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.CategoryMapper;
import com.group3.sp25hsf302group3se1889vj.repository.CategoryRepository;
import com.group3.sp25hsf302group3se1889vj.service.CategoryService;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.specification.CategorySpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    @Override
    public List<CategoryDTO> getParentCategories() {
        var parentCategories = categoryRepository.findByParentIsNull();
        return parentCategories.stream()
                .map(categoryMapper::mapToCategoryDTO)
                .toList();
    }

    @Override
    public Page<CategoryDTO> searchCategories(CategoryFilterDTO categoryFilterDTO, Pageable pageable) {
        Specification<Category> specification = CategorySpecification.filterCategory(categoryFilterDTO);
        return categoryRepository.findAll(specification, pageable)
                .map(categoryMapper::mapToCategoryDTO);
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        log.info("save categoryDTO: {}", categoryDTO);
        var category = categoryMapper.mapToCategory(categoryDTO);
        if (categoryDTO.getParentId() != null) {
            var parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParent(parentCategory);
        }
        categoryRepository.save(category);
        sendCategoryNotification("được tạo", categoryDTO, NotificationType.INFO);
    }

    @Override
    public boolean existsByNameAndParentId(String name, Long parentId) {
        return categoryRepository.existsByNameAndParentId(name, parentId);
    }

    @Override
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::mapToCategoryDTO)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Override
    public boolean existsByNameAndParentIdAndIdNot(String name, Long parentId, Long id) {
        return categoryRepository.existsByNameAndParentIdAndIdNot(name, parentId, id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        log.info("update categoryDTO: {}", categoryDTO);
        var category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getImage());
        if (categoryDTO.getParentId() != null) {
            var parentCategory = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParent(parentCategory);
        } else {
            category.setParent(null);
        }
        categoryRepository.save(category);
        sendCategoryNotification("được cập nhật", categoryDTO, NotificationType.INFO);
    }

    @Override
    public void delete(Long id) {
        log.info("delete category id: {}", id);
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        var categoryDTO = categoryMapper.mapToCategoryDTO(category);
        categoryRepository.deleteById(id);
        sendCategoryNotification("được xóa", categoryDTO, NotificationType.WARNING);
    }

    /**
     * Tạo thông báo cho thao tác trên banner.
     */
    private void sendCategoryNotification(String action, CategoryDTO category, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Danh mục đã " + action);
        notification.setContent("Danh mục \"" + category.getName() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_CATEGORY);
    }


}
