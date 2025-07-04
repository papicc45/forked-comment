package com.weatherfit.comment_service.common.config;

import com.weatherfit.comment_service.coupon.converter.DiscountTypeReadingConverter;
import com.weatherfit.comment_service.coupon.converter.DiscountTypeWritingConverter;
import com.weatherfit.comment_service.coupon.entity.DiscountType;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.DialogTypeSelection;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableR2dbcAuditing
public class R2dbcConfig  {


    @Bean
    public ConnectionFactory connectionFactory() {
        MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
                .host("127.0.0.1")
                .user("root")
                .port(3306)
                .password("!ehdwns12")
                .database("r2dbc")
                .createDatabaseIfNotExist(true)
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        return MySqlConnectionFactory.from(configuration);
    }

    @Bean
    public R2dbcCustomConversions customConversions(ConnectionFactory connectionFactory) {
        R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);

        CustomConversions.StoreConversions storeConversions = CustomConversions.StoreConversions.of(
                dialect.getSimpleTypeHolder(),
                dialect.getConverters()
        );

        List<Object> userConverters = List.of(
                new DiscountTypeReadingConverter(),
                new DiscountTypeWritingConverter()
        );

        return new R2dbcCustomConversions(storeConversions, userConverters);
    }

    @ReadingConverter
    static class DiscountTypeReadingConverter implements Converter<String, DiscountType> {
        @Override
        public DiscountType convert(String dbValue) {
            return DiscountType.valueOf(dbValue);
        }
    }

    @WritingConverter
    static class DiscountTypeWritingConverter implements Converter<DiscountType, String> {
        @Override
        public String convert(DiscountType enumValue) {
            return enumValue.name();
        }
    }
}


