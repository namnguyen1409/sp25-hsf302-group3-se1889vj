package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.ProductDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.ProductFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
import com.group3.sp25hsf302group3se1889vj.service.CategoryService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('OWNER')")
@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    private final MetadataExtractor metadataExtractor;

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
        
        BrandFilterDTO brandFilterDTO = new BrandFilterDTO();
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        try {
            Page<BrandDTO> brandPage = brandService.findAll(brandFilterDTO, pageable);
            model.addAttribute("brands", brandPage != null ? brandPage.getContent() : new ArrayList<>());
        } catch (Exception e) {
            model.addAttribute("brands", new ArrayList<>());

        }

        try {
            List<CategoryDTO> categories = categoryService.getParentCategories();
            model.addAttribute("categories", categories != null ? categories : new ArrayList<>());
        } catch (Exception e) {
            model.addAttribute("categories", new ArrayList<>());

        }

        return "admin/product/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated ProductDTO product,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            // Thêm lại danh sách brands và categories khi có lỗi validation
            BrandFilterDTO brandFilterDTO = new BrandFilterDTO();
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            try {
                Page<BrandDTO> brandPage = brandService.findAll(brandFilterDTO, pageable);
                model.addAttribute("brands", brandPage != null ? brandPage.getContent() : new ArrayList<>());
            } catch (Exception e) {
                model.addAttribute("brands", new ArrayList<>());
            }

            try {
                List<CategoryDTO> categories = categoryService.getParentCategories();
                model.addAttribute("categories", categories != null ? categories : new ArrayList<>());
            } catch (Exception e) {
                model.addAttribute("categories", new ArrayList<>());
            }

            return "admin/product/add";
        }

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
        // TODO: Add more actions here

        if (bindingResult.hasErrors()) {
            return "admin/product/edit";
        }

        productService.update(product);
        return "redirect:/admin/product/";
    }
}