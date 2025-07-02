package com.weatherfit.comment_service.product.service;

import com.weatherfit.comment_service.product.dto.ProductRequestDTO;
import com.weatherfit.comment_service.product.mapper.ProductMapper;
import com.weatherfit.comment_service.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    public Mono<Void> registerProduct(Flux<ProductRequestDTO> dtoFlux) {
        return dtoFlux
                .map(productMapper::DTOToProduct)
                .flatMap(productRepository::save)
                .then();
    }
}
