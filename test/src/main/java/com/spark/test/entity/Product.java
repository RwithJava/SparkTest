package com.spark.test.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Sale> sales;

    public double getRevenue() {
        return sales.stream()
                .mapToDouble(sale -> sale.getQuantity() * this.price).sum();
    }

}
