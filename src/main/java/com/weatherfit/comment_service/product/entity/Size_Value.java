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
@Table("size_value")
public class Size_Value {

    @Id
    private Long id;

    private Long systemId;

    private String code;

    private BigDecimal numericOrder;
}
