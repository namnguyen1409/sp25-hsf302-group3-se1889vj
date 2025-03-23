package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.StaticPageFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.StaticPageService;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
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

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_STATIC_PAGE')")
@Controller
@RequestMapping("/admin/static-page")
@RequiredArgsConstructor
public class StaticPageController {
    private final StaticPageService staticPageService;
    private final MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(Model model,
                       @ModelAttribute(value = "filterDTO", binding = false)StaticPageFilterDTO filterDTO){
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                staticPageService,
                metadataExtractor,
                StaticPageFilterDTO::new,
                StaticPageDTO.class,
                Arrays.asList("title", "slug", "createdBy")
        );
        return "admin/static-page/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") StaticPageFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/static-page/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("entity", new StaticPageDTO());
        return "admin/static-page/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("entity") @Validated StaticPageDTO staticPageDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if(staticPageService.existsBySlug(staticPageDTO.getSlug())){
            result.rejectValue("slug", "slug.duplicate");
        }
        if(staticPageService.existsByTitle(staticPageDTO.getTitle())){
            result.rejectValue("title", "title.duplicate");
        }
        if(result.hasErrors()) {
            return "admin/static-page/add";
        }
        staticPageService.save(staticPageDTO);
        return "redirect:/admin/static-page/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id
    ) {
        StaticPageDTO staticPageDTO = staticPageService.findById(id);
        model.addAttribute("entity", staticPageDTO);
        return "admin/static-page/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("entity") @Validated StaticPageDTO staticPageDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(staticPageService.existsBySlugAndIdNot(staticPageDTO.getSlug(), staticPageDTO.getId())){
            bindingResult.rejectValue("slug", "slug.duplicate", "Slug đã tồn tại");
        }
        if(staticPageService.existsByTitleAndIdNot(staticPageDTO.getTitle(), staticPageDTO.getId())){
            bindingResult.rejectValue("title", "title.duplicate", "Tiêu đề đã tồn tại");
        }
        if(bindingResult.hasErrors()) {
            return "admin/static-page/edit";
        }

        staticPageService.update(staticPageDTO);
        return "redirect:/admin/static-page/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        staticPageService.delete(id);
        return "redirect:/admin/static-page/";
    }

    @GetMapping("/view/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        StaticPageDTO staticPageDTO = staticPageService.findById(id);
        model.addAttribute("entity", staticPageDTO);
        return "admin/static-page/view";
    }

}
