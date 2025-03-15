package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.CouponService;
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
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) CouponFilterDTO filterDTO
    ) {
        if(filterDTO == null) {
            filterDTO = new CouponFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        // TODO: Add more fields here
        List<String> fields = Arrays.asList("id");
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(CouponDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(CouponDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CouponDTO> page = couponService.findAll(filterDTO, pageable);
        model.addAttribute("page", page);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/coupon/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") CouponFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/coupon/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("entity", new CouponDTO());
        return "admin/coupon/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated CouponDTO coupon,
            BindingResult result
    ) {
        // TODO: Add more actions here

        if(result.hasErrors()) {
            return "admin/coupon/add";
        }
        couponService.save(coupon);
        return "redirect:/admin/coupon/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                        @PathVariable("id") Long id
    ) {
        CouponDTO coupon = couponService.findById(id);
        model.addAttribute("entity", coupon);
        return "admin/coupon/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("entity") @Validated CouponDTO coupon,
            BindingResult bindingResult

    ) {
        // TODO: Add more actions here

        if(bindingResult.hasErrors()) {
            return "admin/coupon/edit";
        }

        couponService.update(coupon);
        return "redirect:/admin/coupon/";
    }
}