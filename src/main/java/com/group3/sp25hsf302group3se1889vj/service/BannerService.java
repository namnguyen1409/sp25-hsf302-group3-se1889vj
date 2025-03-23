package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;


public interface BannerService extends PagingService<BannerDTO, BannerFilterDTO> {

    void save(BannerDTO banner);

    BannerDTO findById(Long id);

    void update(BannerDTO banner);

    void delete(Long id);
}