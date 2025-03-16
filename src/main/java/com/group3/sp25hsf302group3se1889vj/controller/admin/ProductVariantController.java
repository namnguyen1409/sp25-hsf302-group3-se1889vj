package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductVariantDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductVariantFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.ProductImageService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.service.ProductVariantService;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/product/{productId}/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;
    private final MetadataExtractor metadataExtractor;
    private final ProductImageService productImageService;
    private final ProductService productService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) ProductVariantFilterDTO filterDTO,
            @PathVariable("productId") Long productId) {
        if (filterDTO == null) {
            filterDTO = new ProductVariantFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();


        List<String> fields = Arrays.asList("size", "color", "quantity","createdAt","createdBy", "updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(ProductVariantDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(ProductVariantDTO.class, fields));

        filterDTO.setProductId(productId);

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<ProductVariantDTO> page = productVariantService.findAll(filterDTO, pageable);

        ProductDTO productDTO = productService.findById(productId);
        model.addAttribute("pages", page);
        model.addAttribute("filterDTO", filterDTO);
        model.addAttribute("productVariant", new ProductVariantDTO());
        model.addAttribute("product", productDTO);
        return "admin/product/variant/list";
    }


    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") ProductVariantFilterDTO filterDTO,
            @PathVariable("productId") Long productId,
            RedirectAttributes redirectAttributes) {
        filterDTO.setProductId(productId);
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/product/" + productId + "/variants/list";
    }

    @GetMapping("/add")
    public String add(Model model, @PathVariable("productId") Long productId) {
        model.addAttribute("productVariant", new ProductVariantDTO());
        return "admin/product/variant/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute @Validated ProductVariantDTO productVariantDTO,
            BindingResult result,
            @PathVariable("productId") Long productId
    ) {
        productVariantDTO.setProductId(productId);
        if (productVariantService.existsByProductIdAndSizeAndColor(productId, productVariantDTO.getSize(), productVariantDTO.getColor())) {
            result.rejectValue("size", "error.productVariant", "Size và màu đã tồn tại");
        }
        if (result.hasErrors()) {
            return "admin/product/variant/add";
        }
        productVariantService.save(productVariantDTO);
        return "redirect:/admin/product/" + productId + "/variants";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id, @PathVariable("productId") Long productId) {
        ProductVariantDTO productVariantDTO = productVariantService.findById(id);
        model.addAttribute("productVariant", productVariantDTO);
        model.addAttribute("product", productService.findById(productId));
        return "admin/product/variant/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute @Validated ProductVariantDTO productVariantDTO,
            BindingResult result,
            @PathVariable("productId") Long productId
    ) {
        if (productVariantService.existsByProductIdAndSizeAndColorAndIdNot(productId, productVariantDTO.getSize(), productVariantDTO.getColor(), productVariantDTO.getId())) {
            result.rejectValue("size", "error.productVariant", "Size và màu đã tồn tại");
        }
        if (result.hasErrors()) {
            return "admin/product/variant/edit";
        }

        productVariantService.update(productVariantDTO);
        return "redirect:/admin/product/" + productId + "/variants";
    }

}
