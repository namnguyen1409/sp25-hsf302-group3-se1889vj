package com.group3.sp25hsf302group3se1889vj.util;

import com.group3.sp25hsf302group3se1889vj.entity.User;
import com.group3.sp25hsf302group3se1889vj.security.userdetails.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static User getUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }

    public static String getCurrentUsername() {
        return getUser().getUsername();
    }

}
