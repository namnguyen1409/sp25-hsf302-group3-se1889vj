package com.group3.sp25hsf302group3se1889vj.service;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CustomerProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerProductSearchDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> findAll(ProductFilterDTO filterDTO, Pageable pageable);

    void save(ProductDTO product);

    ProductDTO findById(Long id);

    void update(ProductDTO product);

    boolean existsByCategoryId(Long id);

    List<CustomerProductDTO> getNewProducts(int i);

    Page<CustomerProductDTO> searchProducts(CustomerProductSearchDTO filterDTO, Pageable pageable);

    CustomerProductDTO getProductById(Long id);

    boolean isProductActive(Long id);

}