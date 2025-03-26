package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_BRAND')")
@Controller
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final MetadataExtractor metadataExtractor;
    private final StorageService storageService;
    private final ProductService productService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) BrandFilterDTO filterDTO
    ) {
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                brandService,
                metadataExtractor,
                BrandFilterDTO::new,
                BrandDTO.class,
                Arrays.asList("name", "logo", "createdBy")
        );
        return "admin/brand/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") BrandFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/brand/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("entity", new BrandDTO());
        return "admin/brand/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated BrandDTO brand,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if(brandService.existsByName(brand.getName())) {
            result.rejectValue("name", "error.exists", "Tên Thương hiệu đã tồn tại");
        }

        if (result.hasErrors()) {
            return "admin/brand/add";
        }
        var img = storageService.moveToUploads(brand.getLogo());
        if (img == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi upload ảnh", "error");
            return "redirect:/admin/brand/add";
        }
        brand.setLogo(img);

        brandService.save(brand);
        return "redirect:/admin/brand/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id
    ) {
        BrandDTO brand = brandService.findById(id);
        model.addAttribute("entity", brand);
        return "admin/brand/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("entity") @Validated BrandDTO brand,
            BindingResult bindingResult
    ) {
        if (brandService.existsByNameAndIdNot(brand.getName(), brand.getId())) {
            bindingResult.rejectValue("name", "error.exists", "Tên Thương hiệu đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "admin/brand/edit";
        }

        brandService.update(brand);
        return "redirect:/admin/brand/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model,
                       @PathVariable("id") Long id
    ) {
        BrandDTO brand = brandService.findById(id);
        if (brand == null) {
            return "redirect:/admin/brand/";
        }

        model.addAttribute("entity", brand);
        return "admin/brand/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
        RedirectAttributes redirectAttributes
    ) {

        if (productService.existsByBrandId(id)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể xóa thương hiệu vì đã có sản phẩm", "error");
            return "redirect:/admin/brand/";
        }

        brandService.delete(id);
        FlashMessageUtil.addFlashMessage(redirectAttributes, "Đã xóa thương hiệu", "success");
        return "redirect:/admin/brand/";
    }


}