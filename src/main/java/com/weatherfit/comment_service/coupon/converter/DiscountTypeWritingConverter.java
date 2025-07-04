package com.weatherfit.comment_service.coupon.converter;

import com.weatherfit.comment_service.coupon.entity.DiscountType;
import org.springframework.core.convert.converter.Converter;

public class DiscountTypeWritingConverter implements Converter<DiscountType, String> {
    @Override
    public String convert(DiscountType enumValue) {
        return enumValue.name();
    }
}