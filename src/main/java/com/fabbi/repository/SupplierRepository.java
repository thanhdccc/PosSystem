package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

	Supplier findOneById(Integer id);
	
	Boolean existsByEmail(String email);
    
    Boolean existsByPhone(String phone);
    
    Boolean existsByPhoneAndIdNot(String phone, Integer id);
    
    Boolean existsByEmailAndIdNot(String email, Integer id);
    
    List<Supplier> findAllBy(Pageable pageable);
    
    @Query("SELECT s FROM Supplier s WHERE s.phone LIKE %:keyword%"
    		+ " OR s.name LIKE %:keyword%"
    		+ " OR s.city LIKE %:keyword%"
    		+ " OR s.address LIKE %:keyword%"
    		+ " OR s.email LIKE %:keyword%")
    List<Supplier> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(s.id) FROM Supplier s WHERE s.phone LIKE %:keyword%"
    		+ " OR s.name LIKE %:keyword%"
    		+ " OR s.city LIKE %:keyword%"
    		+ " OR s.address LIKE %:keyword%"
    		+ " OR s.email LIKE %:keyword%")
    Integer countByKeyword(@Param("keyword") String keyword);
}
