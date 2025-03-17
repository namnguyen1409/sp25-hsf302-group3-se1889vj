package com.group3.sp25hsf302group3se1889vj.handler;



import com.group3.sp25hsf302group3se1889vj.dto.BrandDTO;
import com.group3.sp25hsf302group3se1889vj.dto.CategoryDTO;
import com.group3.sp25hsf302group3se1889vj.dto.StaticPageDTO;
import com.group3.sp25hsf302group3se1889vj.entity.StaticPage;
import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import com.group3.sp25hsf302group3se1889vj.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAttributes {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final NotificationService notificationService;
    private final StaticPageService staticPageService;
    private final ShoppingCartService shoppingCartService;

    @Value("${recaptcha.key.site}")
    private String recaptchaSiteKey;

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

    @ModelAttribute("brands")
    public List<BrandDTO> getBrands() {
        return brandService.getBrands();
    }

    @ModelAttribute("unreadNotificationsCount")
    public int getUnreadNotificationsCount() {
        try{
            return notificationService.countUnreadNotificationsByUserId(getCurrentUser().getId());
        } catch (Exception e) {
            return 0;
        }
    }

    @ModelAttribute("staticPage")
    public List<StaticPageDTO> getStaticPage() {
        return staticPageService.getStaticPage();
    }

    @ModelAttribute("cartCount")
    public int getCartCount() {
        try {
            return shoppingCartService.countTotalQuantityByUsername(getCurrentUser().getUsername());
        } catch (Exception e) {
            return 0;
        }
    }

}
