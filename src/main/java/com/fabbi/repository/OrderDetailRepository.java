package com.fabbi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	List<OrderDetail> findAllByOrderId(Integer id);
	
	@Query(name = "find_best_sold", nativeQuery = true)
	List<ReportBestSoldDTO> getBestSold();
}
