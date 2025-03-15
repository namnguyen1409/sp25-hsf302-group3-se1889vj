package com.group3.sp25hsf302group3se1889vj.exception;

/**
 * Lỗi 404
 * Đây là một ngoại lệ được sử dụng khi không tìm thấy dữ liệu
 */
public class Http404 extends RuntimeException {
    public Http404(String message) {
        super(message);
    }
}
