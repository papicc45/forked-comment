package com.weatherfit.comment_service.product.mapper;

import com.weatherfit.comment_service.common.util.DateTimeFormatterUtil;
import com.weatherfit.comment_service.product.dto.ProductRequestDTO;
import com.weatherfit.comment_service.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DateTimeFormatterUtil.class)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    Product DTOToProduct(ProductRequestDTO productRequestDTO);
}
