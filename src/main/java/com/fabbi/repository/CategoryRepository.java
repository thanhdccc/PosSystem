package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Category findOneById(Integer id);

	Boolean existsByName(String name);
    
    Boolean existsByNameAndIdNot(String name, Integer id);
    
    List<Category> findAllBy(Pageable pageable);
    
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:name%")
    List<Category> findByName(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT COUNT(c.id) FROM Category c WHERE c.name LIKE %:name%")
    Integer countByName(@Param("name") String name);
}
