package com.weatherfit.comment_service.common.exception;

public enum ErrorCode {
    AUTH_DUPLICATE_EMAIL("AUTH-001", "이미 가입된 이메일입니다"),
    EXPIRED_TOKEN("AUTH-002", "토큰 유효기간이 경과하였습니다."),
    AUTH_NUMBER_NOT_MATCH("AUTH-003", "토큰 인증값이 일치하지 않습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() { return  message; }
    public String getCode() { return code; }
}
