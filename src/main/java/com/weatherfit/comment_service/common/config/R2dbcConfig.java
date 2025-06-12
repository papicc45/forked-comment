package com.weatherfit.comment_service.common.config;

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
@EnableR2dbcAuditing
public class R2dbcConfig {
    MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
            .host("127.0.0.1")
            .user("root")
            .port(3306)
            .password("!ehdwns12")
            .database("r2dbc")
            .createDatabaseIfNotExist(true)
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    ConnectionFactory connectionFactory = MySqlConnectionFactory.from(configuration);
    Mono<Connection> connectionMono = Mono.from(connectionFactory.create());
}
