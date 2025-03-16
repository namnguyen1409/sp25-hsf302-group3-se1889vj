package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductImageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.*;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@PreAuthorize("hasRole('OWNER')")
@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    private final MetadataExtractor metadataExtractor;
    private final StorageService storageService;
    private final ProductImageService productImageService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) ProductFilterDTO filterDTO
    ) {
        if (filterDTO == null) {
            filterDTO = new ProductFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();


        List<String> fields = Arrays.asList("name", "description", "priceOrigin", "priceSale","thumbnail","brandName",
                                            "categoryName","createdAt","createdBy", "updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(ProductDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(ProductDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<ProductDTO> page = productService.findAll(filterDTO, pageable);
        model.addAttribute("pages", page);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/product/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") ProductFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/product/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("entity", new ProductDTO());
        return "admin/product/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated ProductDTO product,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "admin/product/add";
        }
        var img = storageService.moveToUploads(product.getThumbnail());
        if (img == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi upload ảnh", "error");
            return "redirect:/admin/product/add";
        }
        product.setThumbnail(img);
        productService.save(product);
        return "redirect:/admin/product/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id
    ) {
        ProductDTO product = productService.findById(id);
        model.addAttribute("entity", product);
        return "admin/product/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("entity") @Validated ProductDTO product,
            BindingResult bindingResult

    ) {
        if (bindingResult.hasErrors()) {
            return "admin/product/edit";
        }
        productService.update(product);
        return "redirect:/admin/product/";
    }

    @GetMapping("/{id}/images")
    public String images(Model model, @PathVariable Long id) {
        var product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/product";
        }
        var productImages = productImageService.findAllByProductId(id);

        // Sort images by position
        List<ProductImageDTO> mutableProductImages = new ArrayList<>(productImages);
        mutableProductImages.sort(Comparator.comparingInt(ProductImageDTO::getPosition));


        model.addAttribute("product", product);
        model.addAttribute("productImages", mutableProductImages);
        model.addAttribute("productImageDTO", new ProductImageDTO());
        return "admin/product/images";
    }

    @PostMapping("/{id}/images/add")
    public String addImage(
            @PathVariable Long id,
            @ModelAttribute("productImageDTO") ProductImageDTO productImageDTO,
            RedirectAttributes redirectAttributes
    ) {
        if (productImageDTO.getUrl() == null || productImageDTO.getUrl().isEmpty()) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "URL ảnh không hợp lệ", "error");
            return "redirect:/admin/product/" + id + "/images";
        }

        log.info("Product Image URL: " + productImageDTO.getUrl());

        var img = storageService.moveToUploads(productImageDTO.getUrl());
        if (img == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi upload ảnh", "error");
            return "redirect:/admin/product/" + id + "/images";
        }

        try {
            productImageDTO.setProductId(id);
            productImageDTO.setUrl(img);
            productImageService.save(productImageDTO);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Thêm ảnh thành công", "success");
        } catch (Exception e) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi khi lưu ảnh", "error");
            log.error("Error when saving product image: " + e.getMessage());
        }

        return "redirect:/admin/product/" + id + "/images";
    }


    @PostMapping("/images/update-order")
    @ResponseBody
    public ResponseEntity<String> updateOrder(@RequestBody List<Long> imageIds) {
        productImageService.updateImageOrder(imageIds);
        return ResponseEntity.ok("Cập nhật thứ tự thành công");
    }

    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long imageId, @RequestParam Long productId) {
        productImageService.deleteImage(imageId);
        return "redirect:/products/" + productId + "/images";
    }

}