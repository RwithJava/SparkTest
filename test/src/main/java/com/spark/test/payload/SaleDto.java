package com.spark.test.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product Information")
public class SaleDto {

    @NotEmpty
    @Size(min = 1, message = "must add quantity")
    private int quantity;

    @NotEmpty
    @Size(min = 1, message = "must add id")
    private Long productId;
}
