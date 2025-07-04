package com.weatherfit.comment_service.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("coupon_template")
public class CouponTemplate {

    @Id
    private Long id;

    private String couponTitle;

    @Column("type")
    private DiscountType discountType;
}
