package com.weatherfit.comment_service.coupon.converter;

import com.weatherfit.comment_service.coupon.entity.TemplateStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class TemplateStatusWritingConverter implements Converter<TemplateStatus, String> {
    @Override
    public String convert(TemplateStatus enumValue) {
        return enumValue.name();
    }
}
