package com.weatherfit.comment_service.product.controller;


import com.weatherfit.comment_service.product.dto.ProductRequestDTO;
import com.weatherfit.comment_service.product.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/product")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> registerProduct(@RequestBody  Flux<ProductRequestDTO> dtoFlux) {
        return productService.registerProduct(dtoFlux);
    }
}
