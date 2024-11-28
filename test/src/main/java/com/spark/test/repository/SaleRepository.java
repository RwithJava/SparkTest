package com.spark.test.repository;

import com.spark.test.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale,Integer> {
}
