package com.weatherfit.comment_service.product.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductRequestDTO {

    private String productName;

    private Long productNumber;

    private String brandName;

    private int gender;

    private int stockCount;

    private Long categoryId;
}
