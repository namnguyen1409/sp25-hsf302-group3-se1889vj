package com.group3.sp25hsf302group3se1889vj.exception;

/**
 * Lỗi 400 Bad Request
 * Đây là một lỗi HTTP mà máy chủ không thể hoặc không muốn xử lý yêu cầu do cú pháp không chính xác.
 */
public class Http400 extends RuntimeException {
    public Http400(String message) {
        super(message);
    }
}
