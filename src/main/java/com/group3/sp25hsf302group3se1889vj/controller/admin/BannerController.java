package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.BannerService;
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
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) BannerFilterDTO filterDTO
    ) {
        if(filterDTO == null) {
            filterDTO = new BannerFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        // TODO: Add more fields here
        List<String> fields = Arrays.asList("id");
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(BannerDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(BannerDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<BannerDTO> page = bannerService.findAll(filterDTO, pageable);
        model.addAttribute("page", page);
        model.addAttribute("filterDTO", filterDTO);
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
            BindingResult result
    ) {
        // TODO: Add more actions here

        if(result.hasErrors()) {
            return "admin/banner/add";
        }
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
            BindingResult bindingResult

    ) {
        if(bindingResult.hasErrors()) {
            return "admin/banner/edit";
        }

        bannerService.update(banner);
        return "redirect:/admin/banner/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        bannerService.delete(id);
        return "redirect:/admin/banner/";
    }
}