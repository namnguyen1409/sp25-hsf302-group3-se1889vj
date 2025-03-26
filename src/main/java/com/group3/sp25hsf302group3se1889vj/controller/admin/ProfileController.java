package com.group3.sp25hsf302group3se1889vj.controller.admin;

import com.group3.sp25hsf302group3se1889vj.dto.ChangePasswordDTO;
import com.group3.sp25hsf302group3se1889vj.dto.UpdateProfileDTO;
import com.group3.sp25hsf302group3se1889vj.mapper.UserMapper;
import com.group3.sp25hsf302group3se1889vj.service.StorageService;
import com.group3.sp25hsf302group3se1889vj.service.UserService;
import com.group3.sp25hsf302group3se1889vj.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final StorageService storageService;

    @GetMapping("/admin/profile")
    public String profile(){
        return "admin/profile";
    }

    @GetMapping("/admin/profile/edit")
    public String edit(
            Model model
    ){
        UpdateProfileDTO updateProfileDTO = userMapper.mapToUpdateProfileDTO(SecurityUtil.getUser());
        model.addAttribute("updateProfileDTO", updateProfileDTO);
        return "admin/profile-edit";
    }

    @PostMapping("/admin/profile/edit")
    public String update(
            @ModelAttribute("updateProfileDTO") @Validated UpdateProfileDTO updateProfileDTO,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "admin/profile-edit";
        }
        updateProfileDTO.setId(SecurityUtil.getUser().getId());
        var image = storageService.moveToUploads(updateProfileDTO.getAvatar());
        if (image != null) {
            updateProfileDTO.setAvatar(image);
        }
        userService.updateProfile(updateProfileDTO);
        return "redirect:/admin/profile";
    }

    @GetMapping("/admin/change-password")
    public String changePassword(
            Model model
    ){
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "admin/change-password";
    }

    @PostMapping("/admin/change-password")
    public String changePassword(
            @ModelAttribute("changePasswordDTO") @Validated ChangePasswordDTO changePasswordDTO,
            BindingResult bindingResult
    ){
        changePasswordDTO.setId(SecurityUtil.getUser().getId());
        if(!userService.checkPassword(changePasswordDTO)){
            bindingResult.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không đúng");
        }

        if(bindingResult.hasErrors()){
            return "admin/change-password";
        }

        userService.changePassword(changePasswordDTO);
        return "redirect:/admin/profile";
    }

}
