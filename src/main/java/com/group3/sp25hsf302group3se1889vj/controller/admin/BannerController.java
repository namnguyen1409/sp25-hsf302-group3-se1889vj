package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BannerService;
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

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_BANNERS')")
@Controller
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final MetadataExtractor metadataExtractor;
    private final StorageService storageService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) BannerFilterDTO filterDTO
    ) {
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                bannerService,
                metadataExtractor,
                BannerFilterDTO::new,
                BannerDTO.class,
                Arrays.asList("title", "url", "image")
        );
        return "admin/banner/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") BannerFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/banner/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("entity", new BannerDTO());
        return "admin/banner/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated BannerDTO banner,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if(result.hasErrors()) {
            return "admin/banner/add";
        }
        var img = storageService.moveToUploads(banner.getImage());
        if (img == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi upload ảnh", "error");
            return "redirect:/admin/banner/add";
        }
        banner.setImage(img);
        bannerService.save(banner);
        return "redirect:/admin/banner/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                        @PathVariable("id") Long id
    ) {
        BannerDTO banner = bannerService.findById(id);
        model.addAttribute("entity", banner);
        return "admin/banner/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("entity") @Validated BannerDTO banner,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "admin/banner/edit";
        }
        var img = storageService.moveToUploads(banner.getImage());
        if (img == null) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Lỗi upload ảnh", "error");
            return "redirect:/admin/banner/edit/" + banner.getId();
        }
        banner.setImage(img);
        bannerService.update(banner);
        return "redirect:/admin/banner/";
    }

    @GetMapping("/view/{id}")
    public String view(Model model,
                        @PathVariable("id") Long id
    ) {
        BannerDTO banner = bannerService.findById(id);
        model.addAttribute("entity", banner);
        return "admin/banner/view";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        bannerService.delete(id);
        return "redirect:/admin/banner/";
    }


}