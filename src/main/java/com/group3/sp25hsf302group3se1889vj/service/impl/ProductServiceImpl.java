package com.group3.sp25hsf302group3se1889vj.service.impl;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CustomerProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.NotificationDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CustomerProductSearchDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.entity.Product;
import com.group3.sp25hsf302group3se1889vj.enums.NotificationType;
import com.group3.sp25hsf302group3se1889vj.enums.PermissionType;
import com.group3.sp25hsf302group3se1889vj.mapper.ProductMapper;
import com.group3.sp25hsf302group3se1889vj.repository.BrandRepository;
import com.group3.sp25hsf302group3se1889vj.repository.CategoryRepository;
import com.group3.sp25hsf302group3se1889vj.repository.ProductRepository;
import com.group3.sp25hsf302group3se1889vj.service.NotificationService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.specification.CustomerProductSpecification;
import com.group3.sp25hsf302group3se1889vj.specification.ProductSpecification;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final NotificationService notificationService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    public Page<ProductDTO> findAll(ProductFilterDTO productFilterDTO, Pageable pageable) {
        Specification<Product> specification = ProductSpecification.filterProduct(productFilterDTO);
        return productRepository.findAll(specification, pageable)
                .map(productMapper::mapToProductDTO);
    }

    @Override
    public void save(ProductDTO product) {
        var entity = productMapper.mapToProduct(product);
        var category = categoryRepository.findById(product.getCategoryId());
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy danh mục");
        }
        if (category.get().getParent() == null) {
            throw new IllegalArgumentException("Phải chọn danh mục con");
        }
        entity.setCategory(category.get());

        var brand = brandRepository.findById(product.getBrandId());
        if (brand.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thương hiệu");
        }
        entity.setBrand(brand.get());
        productRepository.save(entity);
        sendProductNotification("được tạo", product, NotificationType.INFO);

    }

    @Override
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::mapToProductDTO)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
    }

    @Override
    public void update(ProductDTO product) {
        var entity = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
        entity.setName(product.getName());
        entity.setPriceOrigin(product.getPriceOrigin());
        entity.setPriceSale(product.getPriceSale());
        entity.setQuantity(product.getQuantity());
        entity.setDescription(product.getDescription());
        entity.setCategory(categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục")));
        entity.setBrand(brandRepository.findById(product.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thương hiệu")));
        productRepository.save(entity);
        sendProductNotification("được cập nhật", product, NotificationType.INFO);
    }

    @Override
    public boolean existsByCategoryId(Long id) {
        return productRepository.existsByCategoryId(id);
    }

    @Override
    public List<CustomerProductDTO> getNewProducts(int i) {
        Pageable pageable = PageRequest.of(0, i);
        return productRepository.findTop10ByIsActiveTrueOrderByCreatedAtDesc()
                .stream().map(productMapper::mapToCustomerProductDTO).toList();
    }

    @Override
    public Page<CustomerProductDTO> searchProducts(CustomerProductSearchDTO filterDTO, Pageable pageable) {

        Specification<Product> specification = CustomerProductSpecification.filterProducts(filterDTO);
        return productRepository.findAll(specification, pageable)
                .map(productMapper::mapToCustomerProductDTO);
    }

    @Override
    public CustomerProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::mapToCustomerProductDTO)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
    }

    @Override
    public boolean isProductActive(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id).isPresent();
    }

    private void sendProductNotification(String action, ProductDTO product, NotificationType type) {
        var username = SecurityUtil.getCurrentUsername();
        NotificationDTO notification = new NotificationDTO();
        notification.setType(type);
        notification.setTitle("Sản phẩm đã " + action);
        notification.setContent("Sản phẩm \"" + product.getName() + "\" đã " + action + " bởi " + username);
        notificationService.sendNotificationToPermission(notification, PermissionType.MANAGE_PRODUCTS);
    }

}
