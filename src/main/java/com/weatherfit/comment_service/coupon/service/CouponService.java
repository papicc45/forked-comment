package com.weatherfit.comment_service.coupon.service;


import reactor.core.publisher.Mono;

public interface CouponService {
    public Mono<Integer> calDiscountAmount(long couponId, long productId);
}
