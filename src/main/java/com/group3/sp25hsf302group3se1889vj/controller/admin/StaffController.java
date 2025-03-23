package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.UserDTO;
import com.group3.sp25hsf302group3se1889vj.dto.filter.UserFilterDTO;
import com.group3.sp25hsf302group3se1889vj.enums.RoleType;
import com.group3.sp25hsf302group3se1889vj.service.PermissionService;
import com.group3.sp25hsf302group3se1889vj.service.TokenService;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.FlashMessageUtil;
import com.group3.sp25hsf302group3se1889vj.util.MetadataExtractor;
import com.group3.sp25hsf302group3se1889vj.util.PaginationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Slf4j
@PreAuthorize("hasRole('OWNER') or hasAnyAuthority('MANAGE_STAFF')")
@Controller
@RequestMapping("/admin/staff")
@AllArgsConstructor
public class StaffController {
    private final TokenService tokenService;
    private final PermissionService permissionService;
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
        filterDTO.setRole(RoleType.STAFF);
        PaginationUtil.setupPagination(
                model,
                filterDTO,
                userService,
                metadataExtractor,
                UserFilterDTO::new,
                UserDTO.class,
                Arrays.asList("username", "firstName", "lastName", "email", "phone", "address", "avatar")
        );
        return "admin/staff/list";
    }

    @PostMapping({"/list", "", "/"})
    public String list(
            @ModelAttribute(value = "filterDTO") UserFilterDTO filterDTO,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("filterDTO", filterDTO);
        return "redirect:/admin/staff/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("userDTO", userDTO);
        return "admin/staff/detail";
    }

    @GetMapping("/permission/{id}")
    public String permission(Model model, @PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        var permissions = permissionService.findAll();
        model.addAttribute("permissions", permissions);
        model.addAttribute("userDTO", userDTO);
        return "admin/staff/permission";
    }

    @PostMapping("/permission")
    public String permission(
            @RequestParam("id") Long id,
            @RequestParam("permissions") List<Long> permissionIds
    ) {
        log.info("id: {}, permissionIds: {}", id, permissionIds);
        userService.changePermissions(id, permissionIds);
        return "redirect:/admin/staff";
    }

    @GetMapping("/add")
    public String add(Model model) {
        return "admin/staff/add";
    }

    @PostMapping("/add")
    public String add(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes
    ) {
        if (userService.existsByEmail(email)) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Địa chỉ email đã tồn tại trước đó", "error");
            return "redirect:/admin/staff/add";
        }
        try{
            userService.inviteStaff(email);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Gửi lời mời thành công", "success");
        }
        catch (Exception e){
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Có lỗi xảy ra khi gửi lời mời hoặc lời mời chưa hết hạn", "error");
        }
        return "redirect:/admin/staff";
    }

    @GetMapping("/lock/{id}")
    public String lock(@PathVariable Long id,
                       @RequestParam("reason") String reason,
                          RedirectAttributes redirectAttributes
                       ) {
        var user = userService.getUserById(id);
        if (user.getRole() != RoleType.STAFF) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể khóa tài khoản này", "error");
            return "redirect:/admin/staff";
        }
        try {
            userService.lockUser(id, reason);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Khóa tài khoản thành công", "success");
        }
        catch (Exception e) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, e.getMessage(), "error");
        }
        return "redirect:/admin/staff";
    }

    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable Long id,
                          RedirectAttributes redirectAttributes
    ) {
        var user = userService.getUserById(id);
        if (user.getRole() != RoleType.STAFF) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Không thể mở khóa tài khoản này", "error");
            return "redirect:/admin/staff";
        }
        try {
            userService.unlockUser(id);
            FlashMessageUtil.addFlashMessage(redirectAttributes, "Mở khóa tài khoản thành công", "success");
        }
        catch (Exception e) {
            FlashMessageUtil.addFlashMessage(redirectAttributes, e.getMessage(), "error");
        }
        return "redirect:/admin/staff";
    }


}
