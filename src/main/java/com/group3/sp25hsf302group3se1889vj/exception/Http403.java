package com.group3.sp25hsf302group3se1889vj.exception;

/**
 * Lỗi 403 - Forbidden
 * Đây là một lớp ngoại lệ được sử dụng để báo lỗi khi người dùng không có quyền truy cập vào một tài nguyên cụ thể.
 */
public class Http403 extends RuntimeException {
    public Http403(String message) {
        super(message);
    }
}
