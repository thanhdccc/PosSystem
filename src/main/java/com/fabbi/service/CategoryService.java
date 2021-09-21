package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.CategoryDTO;

public interface CategoryService {

	Boolean add(CategoryDTO categoryDTO);
	
	Boolean update(CategoryDTO categoryDTO);
	
	CategoryDTO getById(Integer id);
	
	List<CategoryDTO> findPaginated(int pageNo, int pageSize);
	
	Boolean delete(Integer id);
	
	List<CategoryDTO> search(String name, int pageNo, int pageSize);
	
	List<CategoryDTO> findAll();
	
	Boolean isExistByName(String name);
	
	Boolean isExistByNameAndIdNot(String name, Integer id);
	
	Integer count();
	
	Integer countByName(String name);
}
