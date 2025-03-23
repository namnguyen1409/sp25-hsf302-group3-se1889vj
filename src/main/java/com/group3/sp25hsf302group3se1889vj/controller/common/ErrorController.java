package com.group3.sp25hsf302group3se1889vj.controller.common;

import com.group3.sp25hsf302group3se1889vj.exception.Http403;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/403")
    public String accessDenied(
            @RequestParam(value = "message", required = false) String message
    ) {
        throw new Http403(message);
    }

}
