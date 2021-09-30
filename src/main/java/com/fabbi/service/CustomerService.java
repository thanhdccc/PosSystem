package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.CustomerDTO;
import com.fabbi.dto.OrderCustomerDTO;

public interface CustomerService {

	Boolean add(CustomerDTO customer);
	
	CustomerDTO addCustomerFromOrder(OrderCustomerDTO customer);
	
	Boolean update(CustomerDTO customer);
	
	Boolean delete(Integer id);
	
	CustomerDTO getById(Integer id);
	
	List<CustomerDTO> findAll();
	
	List<CustomerDTO> findPaginated(int pageNo, int pageSize);
	
	List<CustomerDTO> search(String keyword, int pageNo, int pageSize);
	
	Boolean isExistByEmail(String email);
	
	Boolean isExistByPhone(String phone);
	
	Boolean isExistByPhoneAndIdNot(String phone, Integer id);
	
	Boolean isExistByEmailAndIdNot(String email, Integer id);
	
	Integer count();
	
	Integer countByKeyword(String keyword);
	
	CustomerDTO getByPhone(String phone);
}
