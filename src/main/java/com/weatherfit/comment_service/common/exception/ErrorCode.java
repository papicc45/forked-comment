package com.weatherfit.comment_service.common.exception;

public enum ErrorCode {
    AUTH_DUPLICATE_EMAIL("AUTH-001", "이미 가입된 이메일입니다");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() { return  message; }
    public String getCode() { return code; }
}
