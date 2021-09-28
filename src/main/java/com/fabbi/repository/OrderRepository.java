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
	
//	@Query("SELECT new com.fabbi.dto.OrderDTO(o.id, o.status, o.amount, c.id, c.name) "
//			+ "FROM Order o INNER JOIN Customer c ON o.customer_Id = c.id "
//			+ "WHERE c.name LIKE %:keyword%")
//	List<OrderDTO> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
//	
//	@Query(value = "SELECT COUNT(*) "
//			+ "FROM orders o INNER JOIN customer c ON o.customer_id = c.id "
//			+ "WHERE c.name LIKE %:keyword%", nativeQuery = true)
//	Integer countByKeyword(@Param("keyword") String keyword);
}
