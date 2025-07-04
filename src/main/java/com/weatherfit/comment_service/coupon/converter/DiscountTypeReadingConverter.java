package com.weatherfit.comment_service.coupon.converter;

import com.weatherfit.comment_service.coupon.entity.DiscountType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class DiscountTypeReadingConverter implements Converter<String, DiscountType> {
    @Override
    public DiscountType convert(String dbValue) {
        return DiscountType.valueOf(dbValue);
    }
}
