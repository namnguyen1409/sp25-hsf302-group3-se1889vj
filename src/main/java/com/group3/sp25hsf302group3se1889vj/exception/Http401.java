package com.group3.sp25hsf302group3se1889vj.exception;

/**
 * Lỗi 401 Unauthorized
 * Đây là lỗi xảy ra khi người dùng không được cấp quyền truy cập vào tài nguyên
 */
public class Http401 extends RuntimeException {
    public Http401(String message) {
        super(message);
    }
}
