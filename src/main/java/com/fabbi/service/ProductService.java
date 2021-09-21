package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.ProductDTO;

public interface ProductService {

	Boolean add(ProductDTO productDTO);
	
	Boolean isExistByCategoryId(Integer id);
	
	Boolean isExistBySupplierId(Integer id);
	
	Boolean isExistByNameAndCategoryId(String name, Integer id);
	
	Boolean isExistByNameAndSupplierId(String name, Integer id);
	
	List<ProductDTO> findPaginated(int pageNo, int pageSize);
	
	Integer count();
}
