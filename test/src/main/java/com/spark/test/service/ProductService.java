package com.spark.test.service;

import com.spark.test.payload.ProductDto;
import com.spark.test.payload.ProductsDto;
import com.spark.test.payload.SaleDto;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto);

    ProductsDto getAllProducts(int page, int size);

    ProductDto getProductById(Long id);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    double getTotalRevenue();

    double getRevenueByProduct(Long productId);

    void addSale(SaleDto saleDto);
}
