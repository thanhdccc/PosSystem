package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Customer findOneById(Integer id);

	Boolean existsByEmail(String email);
    
    Boolean existsByPhone(String phone);
    
    Boolean existsByPhoneAndIdNot(String phone, Integer id);
    
    Boolean existsByEmailAndIdNot(String email, Integer id);
    
    List<Customer> findAllBy(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.phone LIKE %:keyword%"
    		+ " OR c.name LIKE %:keyword%"
    		+ " OR c.type LIKE %:keyword%"
    		+ " OR c.address LIKE %:keyword%"
    		+ " OR c.email LIKE %:keyword%")
    List<Customer> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(c.id) FROM Customer c WHERE c.phone LIKE %:keyword%"
    		+ " OR c.name LIKE %:keyword%"
    		+ " OR c.type LIKE %:keyword%"
    		+ " OR c.address LIKE %:keyword%"
    		+ " OR c.email LIKE %:keyword%")
    Integer countByKeyword(@Param("keyword") String keyword);
}
