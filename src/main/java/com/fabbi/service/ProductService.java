package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.ProductDTO;

public interface ProductService {

	Boolean add(ProductDTO productDTO);
	
	Boolean update(ProductDTO productDTO);
	
	Boolean delete(Integer id);
	
	ProductDTO getById(Integer id);
	
	Boolean isExistByCategoryId(Integer id);
	
	Boolean isExistBySupplierId(Integer id);
	
	Boolean isExistByNameAndCategoryId(String name, Integer id);
	
	Boolean isExistByNameAndSupplierId(String name, Integer id);
	
	Boolean isExistByNameAndCategoryIdAndIdNot(String name, Integer categoryId, Integer id);
	
	Boolean isExistByNameAndSupplierIdAndIdNot(String name, Integer supplierId, Integer id);
	
	List<ProductDTO> findPaginated(int pageNo, int pageSize);
	
	List<ProductDTO> findAll();
	
	List<ProductDTO> search(String keyword, int pageNo, int pageSize);
	
	Integer count();
	
	Integer countByKeyword(String keyword);
}
