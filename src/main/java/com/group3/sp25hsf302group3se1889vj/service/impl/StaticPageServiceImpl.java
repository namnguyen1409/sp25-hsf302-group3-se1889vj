package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.StaticPageFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Banner;
import com.group3.sp25hsf302group3se1889vj.entity.StaticPage;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.exception.Http404;
import com.group3.sp25hsf302group3se1889vj.mapper.StaticPageMapper;
import com.group3.sp25hsf302group3se1889vj.repository.StaticPageRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.service.StaticPageService;
import com.group3.sp25hsf302group3se1889vj.specification.BannerSpecification;
import com.group3.sp25hsf302group3se1889vj.specification.StaticPageSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class StaticPageServiceImpl implements StaticPageService {

    private final StaticPageRepository staticPageRepository;
    private final StaticPageMapper staticPageMapper;
    private final NotificationService notificationService;

    @Override
    public Page<StaticPageDTO> findAll(StaticPageFilterDTO filterDTO, Pageable pageable) {
        Specification<StaticPage> specification = StaticPageSpecification.filterStaticPage(filterDTO);
        return staticPageRepository.findAll(specification, pageable)
                .map(staticPageMapper::toDTO);
    }

    @Override
    public void save(StaticPageDTO staticPageDTO) {
        staticPageRepository.save(staticPageMapper.toEntity(staticPageDTO));
        sendStaticPageNotification("được tạo", staticPageDTO, NotificationType.INFO);
    }

    @Override
    public StaticPageDTO findById(Long id) {
        return staticPageRepository.findById(id)
                .map(staticPageMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Static page not found"));
    }

    @Override
    public void update(StaticPageDTO staticPageDTO) {
        StaticPage entity = staticPageRepository.findById(staticPageDTO.getId())
                .orElseThrow(() -> new RuntimeException("Static page not found"));
        entity.setTitle(staticPageDTO.getTitle());
        entity.setSlug(staticPageDTO.getSlug());
        entity.setContent(staticPageDTO.getContent());
        staticPageRepository.save(entity);
        sendStaticPageNotification("cập nhật", staticPageDTO, NotificationType.INFO);
    }

    @Override
    public void delete(Long id) {
        StaticPageDTO staticPage = findById(id);
        if (staticPage == null) {
            throw new Http404("Trang không tồn tại");
        }
        sendStaticPageNotification("được xóa", staticPage, NotificationType.WARNING);
        staticPageRepository.deleteById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return staticPageRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsByTitle(String title) {
        return staticPageRepository.existsByTitle(title);
    }

    @Override
    public StaticPageDTO findBySlug(String slug) {
        return staticPageRepository.findBySlug((slug))
                .map(staticPageMapper::toDTO)
                .orElseThrow(() -> new Http404("Trang không tồn tại"));
    }

    @Override
    public List<StaticPageDTO> getStaticPage() {
        return staticPageRepository.findAll().stream().map(staticPageMapper::toDTO).toList();
    }

    @Override
    public boolean existsBySlugAndIdNot(String slug, Long id) {
        return staticPageRepository.existsBySlugAndIdNot(slug, id);
    }

    @Override
    public boolean existsByTitleAndIdNot(String title, Long id) {
        return staticPageRepository.existsByTitleAndIdNot(title, id);
    }

    private void sendStaticPageNotification(String action, StaticPageDTO staticPage, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Trang tĩnh đã " + action);
        notification.setContent("Trang tĩnh \"" + staticPage.getTitle() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_STATIC_PAGE);
    }



}
