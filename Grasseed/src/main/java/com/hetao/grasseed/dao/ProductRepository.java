package com.hetao.grasseed.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hetao.grasseed.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	List<Product> findByIsOnlineTrue();

	Product findByProductCode(String productCode);


}
