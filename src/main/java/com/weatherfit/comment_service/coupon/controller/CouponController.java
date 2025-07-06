package com.weatherfit.comment_service.coupon.controller;

import com.weatherfit.comment_service.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    public Mono<Integer> calDiscountAmount(@PathVariable Long couponId, @PathVariable Long productId) {
        return couponService.calDiscountAmount(couponId, productId);
    }
}
