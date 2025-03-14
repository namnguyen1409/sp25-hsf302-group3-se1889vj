package com.group3.sp25hsf302group3se1889vj.handler;



import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import com.group3.sp25hsf302group3se1889vj.service.CategoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@ControllerAdvice
public class GlobalAttributes {
    private final CategoryService categoryService;
    @Value("${recaptcha.key.site}")
    private String recaptchaSiteKey;

    public GlobalAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("recaptchaSiteKey")
    public String getRecaptchaSiteKey() {
        return recaptchaSiteKey;
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        return null;
    }

    @ModelAttribute("categories")
    public List<CategoryDTO> getCategories() {
        return categoryService.getParentCategories();
    }


}
