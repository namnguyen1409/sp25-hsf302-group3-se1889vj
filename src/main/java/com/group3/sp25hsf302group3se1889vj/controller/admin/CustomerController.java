package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_CUSTOMERS')")
@Controller
@RequestMapping("/admin/customer")
@AllArgsConstructor
public class CustomerController {
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
        filterDTO.setRole(RoleType.CUSTOMER);
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                userService,
                metadataExtractor,
                UserFilterDTO::new,
                UserDTO.class,
                Arrays.asList("username", "firstName", "lastName", "email", "phone", "address", "avatar", "role")
        );

        return "admin/customer/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") UserFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/customer/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("userDTO", userDTO);
        return "admin/customer/detail";
    }


    @GetMapping("/lock/{id}")
    public String lock(@PathVariable Long id,
                       @RequestParam("reason") String reason,
                       RedirectAttributes redirectAttributes
    ) {
        var user = userService.getUserById(id);
        if (user.getRole() != RoleType.CUSTOMER) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể khóa tài khoản này", "error");
            return "redirect:/admin/customer";
        }
        try {
            userService.lockUser(id, reason);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Khóa tài khoản thành công", "success");
        }
        catch (Exception e) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, e.getMessage(), "error");
        }
        return "redirect:/admin/customer";
    }

    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable Long id,
                         RedirectAttributes redirectAttributes
    ) {
        var user = userService.getUserById(id);
        if (user.getRole() != RoleType.CUSTOMER) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể mở khóa tài khoản này", "error");
            return "redirect:/admin/customer";
        }
        try {
            userService.unlockUser(id);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Mở khóa tài khoản thành công", "success");
        }
        catch (Exception e) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, e.getMessage(), "error");
        }
        return "redirect:/admin/customer";
    }


}
