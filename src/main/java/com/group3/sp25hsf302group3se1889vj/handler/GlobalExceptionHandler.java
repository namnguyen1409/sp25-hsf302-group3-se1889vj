package com.group3.sp25hsf302group3se1889vj.handler;

import com.group3.sp25hsf302group3se1889vj.exception.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttp400(Model model) {
        model.addAttribute("error",
                new Error("400",
                        "Lỗi yêu cầu",
                        "Xin lỗi, yêu cầu của bạn không hợp lệ."));
        return "common/error";
    }

    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFoundException(
            Model model) {
        model.addAttribute("error",
                new Error("404",
                        "Không tìm thấy trang",
                        "Xin lỗi, trang bạn yêu cầu không tồn tại."));
        return "common/error";
    }

    @ExceptionHandler({Http400.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttp400(Http400 exception,
                                Model model) {
        model.addAttribute("error",
                new Error("400",
                        "Lỗi yêu cầu",
                        exception.getMessage()));
        return "common/error";
    }

    @ExceptionHandler({Http401.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleHttp401(Http401 exception,
                                Model model) {
        model.addAttribute("error",
                new Error("401",
                        "Lỗi xác thực",
                        exception.getMessage()));
        return "common/error";
    }


    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(
                                Model model) {
        model.addAttribute("error",
                new Error("403",
                        "Lỗi truy cập",
                        "Bạn không có quyền truy cập trang này."));
        return "common/error";
    }

    @ExceptionHandler({Http403.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleHttp403(Http403 exception,
                                Model model) {
        model.addAttribute("error",
                new Error("403",
                        "Lỗi truy cập",
                        exception.getMessage()));
        return "common/error";
    }

    @ExceptionHandler({Http404.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleHttp404(Http404 exception,
                                Model model) {
        model.addAttribute("error",
                new Error("404",
                        "Không tìm thấy",
                        exception.getMessage()));
        return "common/error";
    }

    @ExceptionHandler({Http500.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleHttp500(Http500 exception,
                                Model model) {
        model.addAttribute("error",
                new Error("500",
                        "Lỗi hệ thống",
                        exception.getMessage()));
        return "common/error";
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception,
                                Model model) {
        model.addAttribute("error",
                new Error("500",
                        "Lỗi hệ thống",
                        "Đã xảy ra một lỗi không mong muốn. Vui lòng thử lại sau."));
        return "common/error";
    }

    @Data
    @AllArgsConstructor
    static class Error {
        private String code;
        private String title;
        private String message;

    }

}
