package com.group3.sp25hsf302group3se1889vj.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeCustomerController {

    @GetMapping({"/", ""})
    public String home() {
        return "customer/home";
    }



}
