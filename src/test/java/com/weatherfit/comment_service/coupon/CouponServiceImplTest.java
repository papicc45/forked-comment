package com.weatherfit.comment_service.coupon;

import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.coupon.entity.CouponTemplate;
import com.weatherfit.comment_service.coupon.entity.DiscountType;
import com.weatherfit.comment_service.coupon.entity.TemplateStatus;
import com.weatherfit.comment_service.coupon.repository.CouponRepository;
import com.weatherfit.comment_service.coupon.service.CouponServiceImpl;
import com.weatherfit.comment_service.product.entity.Product;
import com.weatherfit.comment_service.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Product product;

    @BeforeEach
    void setUp() {
        long productId = 2L;

        product = new Product();
        product.setId(productId);
        product.setPrice(60000);

        lenient().when(productRepository.findById(2L)).thenReturn(Mono.just(product));
    }
    @Test
    void calDiscountAmount_fixed() {
        CouponTemplate coupon = new CouponTemplate();
        long couponId = 1L;
        coupon = new CouponTemplate();
        coupon.setId(couponId);
        coupon.setDiscountType(DiscountType.FIXED);
        coupon.setDiscountValue(5000);
        coupon.setMaxDiscountAmount(10000);
        coupon.setMinOrderAmount(50000);
        coupon.setValidFrom(LocalDateTime.now().minusDays(1));
        coupon.setValidTo(LocalDateTime.now().plusDays(1));
        coupon.setStatus(TemplateStatus.ACTIVE);

        when(couponRepository.findById(1L)).thenReturn(Mono.just(coupon));

        StepVerifier.create(couponService.calDiscountAmount(couponId, 2L))
                .expectNext(5000)
                .verifyComplete();
    }

    @Test
    void calDiscountAmount_percent_maxDiscount() {
        CouponTemplate coupon = new CouponTemplate();
        coupon.setId(1300L);
        coupon.setDiscountType(DiscountType.PERCENT);
        coupon.setDiscountValue(20);
        coupon.setMaxDiscountAmount(10000);
        coupon.setMinOrderAmount(50000);
        coupon.setValidFrom(LocalDateTime.now().minusDays(1));
        coupon.setValidTo(LocalDateTime.now().plusDays(1));
        coupon.setStatus(TemplateStatus.ACTIVE);

        when(couponRepository.findById(1300L)).thenReturn(Mono.just(coupon));

        StepVerifier.create(couponService.calDiscountAmount(1300L, 2L))
                .expectNext(10000)
                .verifyComplete();
    }

    @Test
    void calDiscountAmount_percent_success() {

    }

    @Test
    void calDiscountAmount_expired_coupon() {
        CouponTemplate coupon = new CouponTemplate();
        coupon.setId(1300L);
        coupon.setDiscountType(DiscountType.PERCENT);
        coupon.setDiscountValue(20);
        coupon.setMaxDiscountAmount(10000);
        coupon.setMinOrderAmount(50000);
        coupon.setValidFrom(LocalDateTime.now().minusMonths(2));
        coupon.setValidTo(LocalDateTime.now().minusMonths(1));
        coupon.setStatus(TemplateStatus.ACTIVE);

        when(couponRepository.findById(1300L)).thenReturn(Mono.just(coupon));
        when(couponRepository.save(any(CouponTemplate.class))).thenReturn(Mono.just(coupon));

        StepVerifier.create(couponService.calDiscountAmount(1300L, 2L))
                .expectErrorSatisfies(throwable -> { /** 던져진 예외 객체 꺼내서 검증*/
                    assertTrue(throwable instanceof BusinessException); /** 이 에러가 맞나 확인*/
                    BusinessException exception = (BusinessException) throwable;
                    assertEquals(ErrorCode.EXPIRED_COUPON, exception.getErrorCode());
                })
                .verify();

        ArgumentCaptor<CouponTemplate> captor = ArgumentCaptor.forClass(CouponTemplate.class);
        verify(couponRepository, times(1)).save(captor.capture());
        assertEquals(TemplateStatus.CLOSED, captor.getValue().getStatus());
    }

    @Test
    void calDiscountAmount_coupon_status_error() {

    }

    @Test
    void calDiscountAmount_min_order_amount() {

    }

}
