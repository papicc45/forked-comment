package com.weatherfit.comment_service.coupon.service;

import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.coupon.entity.DiscountType;
import com.weatherfit.comment_service.coupon.entity.TemplateStatus;
import com.weatherfit.comment_service.coupon.repository.CouponRepository;
import com.weatherfit.comment_service.product.entity.Product;
import com.weatherfit.comment_service.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<Integer> calDiscountAmount(long couponId, long productId) {
        return couponRepository.findById(couponId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.COUPON_NOT_FOUND)))
                .flatMap(coupon -> {
                    LocalDateTime now = LocalDateTime.now();
                    if(now.isAfter(coupon.getValidTo()) || now.isBefore(coupon.getValidFrom())) {
                        coupon.setStatus(TemplateStatus.CLOSED);
                        return couponRepository.save(coupon)
                                .then(Mono.error(new BusinessException(ErrorCode.EXPIRED_COUPON)));
                    }
                    if(coupon.getStatus() != TemplateStatus.ACTIVE) {
                        return Mono.error(new BusinessException(ErrorCode.COUPON_STATUS_NOT_ACTIVE));
                    }
                    return productRepository.findById(productId)
                            .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.PRODUCT_NOT_FOUND)))
                            .flatMap(product -> {
                                if(product.getPrice() < coupon.getMinOrderAmount()) {
                                    return Mono.error(new BusinessException(ErrorCode.MIN_ORDER_AMOUNT));
                                }
                                int discount;
                                if(coupon.getDiscountType() == DiscountType.FIXED) {
                                    discount = coupon.getDiscountValue();
                                } else {
                                    discount = (int) (product.getPrice() * (coupon.getDiscountValue() / 100.0));
                                    if(discount > coupon.getMaxDiscountAmount()) {
                                        discount = coupon.getMaxDiscountAmount();
                                    }
                                }
                                return Mono.just(discount);
                            });
                });
    }
}
