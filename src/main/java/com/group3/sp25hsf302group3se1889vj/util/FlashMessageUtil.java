package com.group3.sp25hsf302group3se1889vj.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class FlashMessageUtil {
    public static void addFlashMessage(RedirectAttributes redirectAttributes, String message, String type) {
        redirectAttributes.addFlashAttribute("flashMessage", message);
        redirectAttributes.addFlashAttribute("flashMessageType", type);
    }
}
