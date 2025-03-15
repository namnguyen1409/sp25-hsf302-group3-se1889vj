package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Banner;
import com.group3.sp25hsf302group3se1889vj.entity.Category;
import com.group3.sp25hsf302group3se1889vj.mapper.BannerMapper;
import com.group3.sp25hsf302group3se1889vj.repository.BannerRepository;
import com.group3.sp25hsf302group3se1889vj.service.BannerService;
import com.group3.sp25hsf302group3se1889vj.specification.BannerSpecification;
import com.group3.sp25hsf302group3se1889vj.specification.CategorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {


    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public Page<BannerDTO> findAll(BannerFilterDTO filterDTO, Pageable pageable) {
        Specification<Banner> specification = BannerSpecification.filterBanner(filterDTO);
        return bannerRepository.findAll(specification, pageable)
                .map(bannerMapper::toDTO);
    }

    @Override
    public void save(BannerDTO banner) {
        bannerRepository.save(bannerMapper.toEntity(banner));
    }

    @Override
    public BannerDTO findById(Long id) {
        return bannerRepository.findById(id)
                .map(bannerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Banner not found"));
    }

    @Override
    public void update(BannerDTO banner) {
        Banner entity = bannerRepository.findById(banner.getId())
                .orElseThrow(() -> new RuntimeException("Banner not found"));
        entity.setTitle(banner.getTitle());
        entity.setUrl(banner.getUrl());
        entity.setImage(banner.getImage());
        bannerRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        bannerRepository.deleteById(id);
    }
}
