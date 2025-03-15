package com.group3.sp25hsf302group3se1889vj.exception;

/**
 * Lôi 500
 * Lôi này sẽ được ném khi có lôi xảy ra ở phía server
 */
public class Http500 extends RuntimeException {
    public Http500(String message) {
        super(message);
    }
}

