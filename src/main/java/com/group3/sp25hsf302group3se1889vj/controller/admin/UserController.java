package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.CategoryFilterDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private MetadataExtractor metadataExtractor;

    @GetMapping({"/list", "", "/"})
    public String list(
            Model model,
            @ModelAttribute(value = "filterDTO", binding = false) UserFilterDTO filterDTO
    ) {
        if(filterDTO == null) {
            filterDTO = new UserFilterDTO();
        }
        Sort sortDirection = "asc".equalsIgnoreCase(filterDTO.getDirection())
                ? Sort.by(filterDTO.getOrderBy()).ascending()
                : Sort.by(filterDTO.getOrderBy()).descending();

        List<String> fields = Arrays.asList("username", "firstName", "lastName", "email", "phone","birthday", "address", "avatar", "role");
        model.addAttribute("fields", fields);
        model.addAttribute("fieldTitles", metadataExtractor.getFieldTitles(UserFilterDTO.class, fields));
        model.addAttribute("fieldClasses", metadataExtractor.getFieldClasses(UserFilterDTO.class, fields));

        Pageable pageable = PageRequest.of(filterDTO.getPage() - 1, filterDTO.getSize(), sortDirection);

        Page<UserDTO> pages = userService.searchUsers(filterDTO, pageable);
        model.addAttribute("pages", pages);
        model.addAttribute("filterDTO", filterDTO);
        return "admin/user/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") UserFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/user/list";
    }
}
