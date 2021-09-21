package com.fabbi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	Boolean existsBySupplierId(Integer id);
	
	Boolean existsByCategoryId(Integer id);
	
	Boolean existsByNameAndSupplierId(String name, Integer id);
	
	Boolean existsByNameAndCategoryId(String name, Integer id);

	Integer countByCategoryId(Integer id);
	
	Integer countBySupplierId(Integer id);
}
