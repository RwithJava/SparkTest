package com.spark.test.service.impl;

import com.spark.test.entity.Product;
import com.spark.test.entity.Sale;
import com.spark.test.exception.ResourceNotFoundException;
import com.spark.test.payload.ProductDto;
import com.spark.test.payload.ProductsDto;
import com.spark.test.payload.SaleDto;
import com.spark.test.repository.ProductRepository;
import com.spark.test.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        log.debug("Entering addProduct method with ProductDto: {}", productDto);
        Product product = modelMapper.map(productDto, Product.class);
        Product newProduct = productRepository.save(product);
        log.info("Product added successfully with ID: {}", newProduct.getId());
        return modelMapper.map(newProduct, ProductDto.class);
    }

    @Override
    public ProductsDto getAllProducts(int page, int size) {

        log.debug("Entering getAllProducts with page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);

        ProductsDto productsDto = new ProductsDto();
        productsDto.setProducts(productPage.getContent().stream().map((product) -> (modelMapper.map(product, ProductDto.class))).toList());
        productsDto.setCurrentPage(productPage.getNumber() + 1);
        productsDto.setTotalElements(productPage.getTotalElements());
        productsDto.setTotalPages(productPage.getTotalPages());
        log.info("Fetched products successfully: {}", productsDto);
        return productsDto;
    }

    @Override
    public ProductDto getProductById(Long id) {
        log.debug("Entering getProductById with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new ResourceNotFoundException("product", "id", id);
                });
        log.info("Fetched product successfully: {}", product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {

        log.debug("Entering updateProduct with ID: {} and ProductDto: {}", id, productDto);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found while updating", id);
                    return new ResourceNotFoundException("product", "id", id);
                });
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        Product savedProduct = productRepository.save(product);
        log.info("Updated successfully {}", savedProduct);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        log.debug("Deleting product with ID: {}", id);
        productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found for deletion", id);
            return new ResourceNotFoundException("Product", "id", id);
        });
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public double getTotalRevenue() {
        log.debug("Entering getTotalRevenue");
        double totalRevenue = productRepository.findAll()
                .stream()
                .mapToDouble(Product::getRevenue)
                .sum();
        log.info("Calculated total revenue: {}", totalRevenue);
        return totalRevenue;
    }

    @Override
    public double getRevenueByProduct(Long productId) {
        log.debug("Entering getRevenueByProduct with productId: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found for ID: {}", productId);
                    return new ResourceNotFoundException("product", "productId", productId);
                });
        double revenue = product.getRevenue();
        log.info("Revenue for product with ID {}: {}", productId, revenue);
        return revenue;
    }

    @Override
    public void addSale(SaleDto saleDto) {
        log.debug("Entering addSale with SaleDto: {}", saleDto);
        Long id = saleDto.getProductId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found for ID while adding sale: {}", id);
                    return new ResourceNotFoundException("product", "id", id);
                });
        Sale sale = new Sale(null, saleDto.getQuantity(), LocalDate.now(), product);
        log.debug("Creating sale for product ID {} with quantity {}", saleDto.getProductId(), saleDto.getQuantity());

        product.getSales().add(sale);
        productRepository.save(product);

        log.info("Sale added and product updated successfully for product ID: {}", id);
    }
}
