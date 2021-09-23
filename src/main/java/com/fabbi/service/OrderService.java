package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.OrderDTO;

public interface OrderService {

	Boolean add(OrderDTO orderDTO);
	
	List<OrderDTO> findPaginated(int pageNo, int pageSize);
	
	Integer count();
}
