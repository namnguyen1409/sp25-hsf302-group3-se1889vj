package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.BannerDTO;
import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.BannerFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.StaticPageFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.StaticPageService;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@PreAuthorize("hasRole('OWNER')")
@Controller
@RequestMapping("/admin/static-page")
@RequiredArgsConstructor
public class StaticPageController {
    private final StaticPageService staticPageService;
    private final MetadataExtractor metadataExtractor;
    private final StorageService storageService;

    @GetMapping({"/list", "", "/"})
    public String list(Model model,
                       @ModelAttribute(value = "filterDTO", binding = false)StaticPageFilterDTO filterDTO){
        if(filterDTO == null) {
            filterDTO = new StaticPageFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("title", "slug", "content", "createdAt", "createdBy","updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(StaticPageDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(StaticPageDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<StaticPageDTO> pages = staticPageService.findAll(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);
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
        if(result.hasErrors()) {
            return "admin/static-page/add";
        }
        staticPageService.save(staticPageDTO);
        return "redirect:/admin/static-page/";
    }
}
