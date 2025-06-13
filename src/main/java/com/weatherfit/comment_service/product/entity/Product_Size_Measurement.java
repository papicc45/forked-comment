package com.weatherfit.comment_service.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("product_size_measurement")
public class Product_Size_Measurement {

    @Id
    private Long id;

    private Long productSizeId;

    private Long attributeId;

    private BigDecimal value;
}
