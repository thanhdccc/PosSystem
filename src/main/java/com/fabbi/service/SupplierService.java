package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.SupplierDTO;

public interface SupplierService {

	Boolean add(SupplierDTO supplierDTO);
	
	Boolean update(SupplierDTO supplierDTO);
	
	SupplierDTO getById(Integer id);
	
	List<SupplierDTO> findPaginated(int pageNo, int pageSize);
	
	Boolean delete(Integer id);
	
	List<SupplierDTO> search(String keyword, int pageNo, int pageSize);
	
	List<SupplierDTO> findAll();
	
	Boolean isExistByEmail(String email);
	
	Boolean isExistByPhone(String phone);
	
	Boolean isExistByPhoneAndIdNot(String phone, Integer id);
	
	Boolean isExistByEmailAndIdNot(String email, Integer id);
	
	Integer count();
	
	Integer countByKeyword(String keyword);
}
