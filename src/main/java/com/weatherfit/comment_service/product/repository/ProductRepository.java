package com.weatherfit.comment_service.product.repository;

import com.weatherfit.comment_service.product.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}
