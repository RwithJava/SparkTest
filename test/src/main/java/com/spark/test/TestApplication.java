package com.spark.test;

import com.spark.test.entity.Product;
import com.spark.test.entity.Sale;
import com.spark.test.payload.ProductDto;
import com.spark.test.payload.SaleDto;
import com.spark.test.repository.ProductRepository;
import com.spark.test.service.ProductService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = " Spark-Test ",
                description = "Machine Test - Spark",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class TestApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(TestApplication.class, args);
        ProductService productService = applicationContext.getBean(ProductService.class);
        ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
        ModelMapper modelMapper = applicationContext.getBean(ModelMapper.class);


        ProductDto p1 = new ProductDto("TV 002", "High-end gaming laptop", 1200, 10);
        ProductDto p2 = new ProductDto("TV 006", "High-end gaming laptop", 2000, 20);

        ProductDto newProduct1 = productService.addProduct(p1);
        ProductDto newProduct2 = productService.addProduct(p2);

        SaleDto sale1 = new SaleDto(10, newProduct1.getId());
        SaleDto sale2 = new SaleDto(20,newProduct2.getId());

        productService.addSale(sale1);
        productService.addSale(sale2);

        System.out.println("------------------------------>");
        double totalRevenue = productService.getTotalRevenue();
        System.out.println("Total Revenue: " + totalRevenue);

        double laptopRevenue = productService.getRevenueByProduct(newProduct1.getId());
        System.out.println("Laptop Revenue: " + laptopRevenue);


    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
