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
public class ProductDto {

    private Long id;

    @NotEmpty
    @Size(min = 2, message = "name must contain 2 letters")
    private String name;

    @NotEmpty
    @Size(min = 10, message = "body must contain 2 letters")
    private String description;

    @NotEmpty
    private double price;

    @NotEmpty
    private int quantity;

    public ProductDto(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
}
