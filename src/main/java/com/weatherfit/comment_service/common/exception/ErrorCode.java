package com.weatherfit.comment_service.common.exception;

public enum ErrorCode {
    AUTH_DUPLICATE_EMAIL("AUTH-001", "이미 가입된 이메일입니다"),
    EXPIRED_TOKEN("AUTH-002", "토큰 유효기간이 경과하였습니다."),
    AUTH_NUMBER_NOT_MATCH("AUTH-003", "토큰 인증값이 일치하지 않습니다."),
    PASSWORD_NOT_MATCH("AUTH-004", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND("AUTH-005", "사용자 존재하지 않습니다."),

    COUPON_NOT_FOUND("COU-001", "쿠폰이 존재하지 않습니다."),
    MIN_ORDER_AMOUNT("COU-002", "최저 금액을 충족하지 못해 쿠폰 적용 불가"),
    COUPON_STATUS_NOT_ACTIVE("COU-003", "쿠폰이 활성화되어 있는 상태가 아님."),
    EXPIRED_COUPON("COU-004", "쿠폰을 사용할 수 있는 기간이 아님"),

    PRODUCT_NOT_FOUND("PRO-001", "상품이 존재하지 않습니다.");
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() { return  message; }
    public String getCode() { return code; }
}
