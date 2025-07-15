package com.weatherfit.comment_service.coupon.controller;

import com.weatherfit.comment_service.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

//    @PostMapping("/issue_coupon")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Void> issueCoupon()

    @PostMapping("/discount_check")
    public Mono<Integer> calDiscountAmount(@PathVariable Long couponId, @PathVariable Long productId) {
        return couponService.calDiscountAmount(couponId, productId);
    }
}
