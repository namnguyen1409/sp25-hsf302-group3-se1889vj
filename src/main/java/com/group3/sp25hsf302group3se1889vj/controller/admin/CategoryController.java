package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CategoryFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.CategoryService;
import com.group3.sp25hsf302group3se1889vj.service.ProductService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_CATEGORY')")
@Controller
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final MetadataExtractor metadataExtractor;
    private final ProductService productService;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) CategoryFilterDTO filterDTO
    ) {
        if (filterDTO == null) {
            filterDTO = new CategoryFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("name", "description", "parentName", "createdAt", "createdBy", "updatedAt", "updatedBy");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(CategoryDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(CategoryDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<CategoryDTO> pages = categoryService.searchCategories(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/category/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") CategoryFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "admin/category/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("categoryDTO") @Validated CategoryDTO categoryDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (categoryService.existsByNameAndParentId(categoryDTO.getName(), categoryDTO.getParentId())) {
            bindingResult.rejectValue("name", "error.categoryDTO", "Tên danh mục đã tồn tại");
        }
        if (bindingResult.hasErrors()) {
            return "admin/category/add";
        }
        categoryService.save(categoryDTO);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") Long id) {
        CategoryDTO categoryDTO = categoryService.findById(id);
        log.info("CategoryDTO: " + categoryDTO);
        model.addAttribute("categoryDTO", categoryDTO);
        return "admin/category/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @ModelAttribute("categoryDTO") @Validated CategoryDTO categoryDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if(Objects.equals(categoryDTO.getParentId(), categoryDTO.getId())) {
            bindingResult.rejectValue("parentId", "error.categoryDTO", "Danh mục cha không thể là chính nó");
        }
        if (categoryService.existsByNameAndParentIdAndIdNot(categoryDTO.getName(), categoryDTO.getParentId(), categoryDTO.getId())) {
            bindingResult.rejectValue("name", "error.categoryDTO", "Tên danh mục đã tồn tại");
        }
        if (bindingResult.hasErrors()) {
            return "admin/category/edit";
        }
        categoryService.update(categoryDTO);
        return "redirect:/admin/category/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes) {
        if (productService.existsByCategoryId(id)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Danh mục này đang chứa sản phẩm, không thể xóa", "error");
            return "redirect:/admin/category/list";
        }
        categoryService.delete(id);
        return "redirect:/admin/category/list";
    }

}
