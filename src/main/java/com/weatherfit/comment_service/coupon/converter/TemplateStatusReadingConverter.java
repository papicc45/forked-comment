package com.weatherfit.comment_service.coupon.converter;

import com.weatherfit.comment_service.coupon.entity.DiscountType;
import com.weatherfit.comment_service.coupon.entity.TemplateStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class TemplateStatusReadingConverter implements Converter<String, TemplateStatus> {

    @Override
    public TemplateStatus convert(String dbValue) {
        return TemplateStatus.valueOf(dbValue);
    }

}
