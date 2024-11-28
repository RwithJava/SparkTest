package com.spark.test.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Pages of products")
public class ProductsDto {

    List<ProductDto> products;
    int currentPage;
    Long totalElements;
    int totalPages;
}
