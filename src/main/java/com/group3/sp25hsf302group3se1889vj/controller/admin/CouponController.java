package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CouponDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CouponFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.CouponType;
import com.group3.sp25hsf302group3se1889vj.service.CouponService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
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

        List<String> fields = Arrays.asList("code", "description", "type", "value", "minOrderValue", "maxDiscount", "maxUsage", "usageCount", "maxUsagePerUser", "startDate", "endDate", "isDeleted");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(CouponDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(CouponDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CouponDTO> pages = couponService.findAll(filterDTO, pageable);
        model.addAttribute("pages", pages);
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

        if (coupon.getStartDate() != null && coupon.getEndDate() != null && coupon.getStartDate().isAfter(coupon.getEndDate())) {
            result.rejectValue("startDate", "error.startDate", "Ngày bắt đầu phải trước ngày kết thúc");
        }

        if (coupon.getType() == CouponType.PERCENTAGE && coupon.getValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            result.rejectValue("value", "error.value", "Giảm giá theo phần trăm không được lớn hơn 100");
        }

        if (couponService.isExistCode(coupon.getCode())) {
            result.rejectValue("code", "error.code", "Mã giảm giá đã tồn tại");
        }

        if(result.hasErrors()) {
            return "admin/coupon/add";
        }

        coupon.setUsageCount(0);

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

        if (coupon.getStartDate() != null && coupon.getEndDate() != null && coupon.getStartDate().isAfter(coupon.getEndDate())) {
            bindingResult.rejectValue("startDate", "error.startDate", "Ngày bắt đầu phải trước ngày kết thúc");
        }

        if (coupon.getType() == CouponType.PERCENTAGE && coupon.getValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            bindingResult.rejectValue("value", "error.value", "Giảm giá theo phần trăm không được lớn hơn 100");
        }

        if (couponService.isExistCodeAndIdNot(coupon.getCode(), coupon.getId())) {
            bindingResult.rejectValue("code", "error.code", "Mã giảm giá đã tồn tại");
        }

        if(bindingResult.hasErrors()) {
            log.info("{}", bindingResult.getAllErrors());
            return "admin/coupon/edit";
        }

        log.info("{}", coupon);

        couponService.update(coupon);
        return "redirect:/admin/coupon/";
    }

    @GetMapping("/view/{id}")
    public String view(Model model,
                        @PathVariable("id") Long id
    ) {
        CouponDTO coupon = couponService.findById(id);
        model.addAttribute("entity", coupon);
        return "admin/coupon/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        couponService.delete(id);
        return "redirect:/admin/coupon/";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable("id") Long id) {
        couponService.restore(id);
        return "redirect:/admin/coupon/";
    }
}