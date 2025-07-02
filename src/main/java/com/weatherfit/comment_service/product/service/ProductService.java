package com.weatherfit.comment_service.product.service;

import com.weatherfit.comment_service.product.dto.ProductRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Void> registerProduct(Flux<ProductRequestDTO> dtoFlux);
}
