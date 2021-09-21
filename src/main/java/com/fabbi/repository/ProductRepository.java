package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
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
	
	List<Product> findAllBy(Pageable pageable);
}
