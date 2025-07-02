package com.weatherfit.comment_service.product;

import com.weatherfit.comment_service.common.config.SecurityConfig;
import com.weatherfit.comment_service.product.controller.ProductController;
import com.weatherfit.comment_service.product.dto.ProductRequestDTO;
import com.weatherfit.comment_service.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class})
@AutoConfigureWebTestClient
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    private ProductRequestDTO dto1;
    private ProductRequestDTO dto2;

    @BeforeEach
    void setUp() {
        dto1 = new ProductRequestDTO("이름1", 10000L, "브랜드1", 0, 100, 9L);
        dto2 = new ProductRequestDTO("이름2", 10001L, "브랜드1", 1, 50, 17L);
    }

    @Test
    void registerProduct_success() {
        when(productService.registerProduct(any(Flux.class)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/product")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(Flux.just(dto1, dto2), ProductRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();

        ArgumentCaptor<Flux<ProductRequestDTO>> captor = ArgumentCaptor.forClass(Flux.class);
        verify(productService, times(1))
                .registerProduct(captor.capture());

        StepVerifier.create(captor.getValue())
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();
    }
}
