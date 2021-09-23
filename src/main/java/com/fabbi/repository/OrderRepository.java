package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findAllBy(Pageable pageable);
}
