package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	Product findOneById(Integer id);
	
	Boolean existsBySupplierId(Integer id);
	
	Boolean existsByCategoryId(Integer id);
	
	Boolean existsByNameAndSupplierId(String name, Integer id);
	
	Boolean existsByNameAndCategoryId(String name, Integer id);
	
	Boolean existsByNameAndSupplierIdAndIdNot(String name, Integer supplierId, Integer id);
	
	Boolean existsByNameAndCategoryIdAndIdNot(String name, Integer categoryId, Integer id);

	Integer countByCategoryId(Integer id);
	
	Integer countBySupplierId(Integer id);
	
	List<Product> findAllBy(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
	List<Product> findByKeyword(String keyword, Pageable pageable);
	
	@Query("SELECT COUNT(p.id) FROM Product p WHERE p.name LIKE %:keyword%")
	Integer countByKeyword(String keyword);
}
