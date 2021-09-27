package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findOneById(Integer id);

	List<Order> findAllBy(Pageable pageable);
	
	@Query("SELECT o FROM Order o WHERE o.status = :keyword")
	List<Order> findByKeyword(@Param("keyword") Integer keyword, Pageable pageable);
	
	@Query("SELECT COUNT(o.id) FROM Order o WHERE o.status = :keyword")
	Integer countByKeyword(@Param("keyword") Integer keyword);
}
