package com.weatherfit.comment_service.coupon.entity;

import com.weatherfit.comment_service.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("coupon_template")
public class CouponTemplate extends BaseEntity {

    @Id
    private Long id;

    private String couponTitle;

    @Column("type")
    private DiscountType discountType;

    private int discountValue;

    private int minOrderAmount;

    private int maxDiscountAmount;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;

    private int totalIssued;

    private int perUserLimit;

    private TemplateStatus status;
}
