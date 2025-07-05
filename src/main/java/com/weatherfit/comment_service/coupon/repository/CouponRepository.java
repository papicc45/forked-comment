package com.weatherfit.comment_service.coupon.repository;

import com.weatherfit.comment_service.coupon.entity.CouponTemplate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CouponRepository extends ReactiveCrudRepository<CouponTemplate, Long> {
}
