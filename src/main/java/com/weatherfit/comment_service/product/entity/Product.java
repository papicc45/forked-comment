package com.weatherfit.comment_service.product.entity;


import com.weatherfit.comment_service.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("product")
public class Product extends BaseEntity {

    @Id
    private Long id;

    private String productName;

    private Long productNumber;

    private int likeCount = 0;

    private Long categoryId;
}
