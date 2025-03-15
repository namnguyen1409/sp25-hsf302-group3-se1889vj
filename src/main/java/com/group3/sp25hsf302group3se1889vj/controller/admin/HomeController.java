package com.group3.sp25hsf302group3se1889vj.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAnyRole('OWNER', 'STAFF')")
@Controller
@RequestMapping("/admin")
public class HomeController {
    @RequestMapping("/home")
    public String home() {
        return "admin/home";
    }
}
