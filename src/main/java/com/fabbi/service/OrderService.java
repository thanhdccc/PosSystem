package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.OrderDTO;

public interface OrderService {
	
	OrderDTO findOneById(Integer id);

	Boolean add(OrderDTO orderDTO);
	
	Boolean update(OrderDTO orderDTO);
	
	List<OrderDTO> findPaginated(int pageNo, int pageSize);
	
	Integer count();
}
