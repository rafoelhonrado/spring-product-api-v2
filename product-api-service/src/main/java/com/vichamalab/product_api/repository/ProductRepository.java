package com.vichamalab.product_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vichamalab.product_api.entity.Product;


public interface ProductRepository extends JpaRepository<Product,Long>{
	Optional<Product> findBySku(String sku);
	Integer deleteBySku(String sku);
	List<Product> findByNameLike(String pattern);
}
