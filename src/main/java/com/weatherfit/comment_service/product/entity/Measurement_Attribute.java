package com.weatherfit.comment_service.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("measurement_attribute")
public class Measurement_Attribute {

    @Id
    private Long id;

    private String name;

    private String unit;
}
