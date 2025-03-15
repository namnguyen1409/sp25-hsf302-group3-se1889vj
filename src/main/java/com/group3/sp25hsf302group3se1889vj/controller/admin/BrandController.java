package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BrandFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BrandService;
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

import java.util.Arrays;
import java.util.List;

@PreAuthorize("hasRole('OWNER')")
@Controller
@RequestMapping("/admin/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) BrandFilterDTO filterDTO
    ) {
        if (filterDTO == null) {
            filterDTO = new BrandFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        // TODO: Add more fields here
        List<String> fields = Arrays.asList("name", "description", "logo", "createdAt", "createdBy","updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(BrandDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(BrandDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<BrandDTO> page = brandService.findAll(filterDTO, pageable);
        model.addAttribute("pages", page);
        model.addAttribute("filterDTO", filterDTO);
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
            BindingResult result
    ) {
        // TODO: Add more actions here

        if (result.hasErrors()) {
            return "admin/brand/add";
        }
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
        // TODO: Add more actions here

        if (bindingResult.hasErrors()) {
            return "admin/brand/edit";
        }

        brandService.update(brand);
        return "redirect:/admin/brand/";
    }
}