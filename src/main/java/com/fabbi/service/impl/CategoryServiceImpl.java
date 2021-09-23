package com.fabbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.dto.CategoryDTO;
import com.fabbi.entity.Category;
import com.fabbi.repository.CategoryRepository;
import com.fabbi.service.CategoryService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public Boolean add(CategoryDTO categoryDTO) {
		log.info("######## Begin insert Category ########");
		
		Category category = ObjectMapperUtils.map(categoryDTO, Category.class);
		
		try {
			categoryRepository.save(category);
		} catch (Exception e) {
			log.error("Cannot insert Category: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert Category ########");
		return true;
	}

	@Override
	public Boolean update(CategoryDTO categoryDTO) {
		log.info("######## Begin update Category ########");
		
		Integer id = categoryDTO.getId();
		
		Category oldCategory = categoryRepository.findOneById(id);
		
		if (oldCategory == null) {
			log.error("Category with id: [" + id + "] not exist");
			return false;
		}
		
		Category newCategory = ObjectMapperUtils.map(categoryDTO, Category.class);
		newCategory.setId(oldCategory.getId());
		
		try {
			categoryRepository.save(newCategory);
		} catch (Exception e) {
			log.error("Cannot update Category: " + e.getMessage());
			return false;
		}
		
		log.info("######## End update Category ########");
		return true;
	}

	@Override
	public CategoryDTO getById(Integer id) {
		log.info("######## Begin get Category by ID ########");
		
		Category category = categoryRepository.findOneById(id);
		
		if (category == null) {
			log.error("Category with id: [" + id + "] not exist");
			return null;
		}
		
		CategoryDTO categoryDTO = ObjectMapperUtils.map(category, CategoryDTO.class);
		categoryDTO.setNumberOfProduct(category.getProducts().size());
		
		log.info("######## End get Category by ID ########");
		return categoryDTO;
	}

	@Override
	public List<CategoryDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Category ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Category> categoryList = categoryRepository.findAllBy(pageable);
		List<CategoryDTO> categoryDTOList = ObjectMapperUtils.mapAll(categoryList, CategoryDTO.class);
		
		for (CategoryDTO itemDTO : categoryDTOList) {
			for (Category item : categoryList) {
				if (itemDTO.getId() == item.getId()) {
					itemDTO.setNumberOfProduct(item.getProducts().size());
				}
			}
		}
		
		log.info("######## End get all Category ########");
		return categoryDTOList;
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete Category with id: [" + id + "] ########");
		
		Category category = categoryRepository.findOneById(id);
		
		if (category == null) {
			log.error("Category with id: [" + id + "] not exist");
			return false;
		}
		
		if (category.getProducts().size() >= 1) {
			log.error("Delete category with id: [" + id + "] failed because there is product belong to.");
			return false;
		}
		
		try {
			categoryRepository.delete(category);
		} catch (Exception e) {
			log.error("Cannot delete Category: " + e.getMessage());
			return false;
		}
		
		log.info("######## End delete Category with id: [" + id + "] ########");
		return true;
	}

	@Override
	public List<CategoryDTO> search(String name, int pageNo, int pageSize) {
		log.info("######## Begin get all Category by name: [" + name + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Category> categoryList = categoryRepository.findByName(name, pageable);
		List<CategoryDTO> categoryDTOList = ObjectMapperUtils.mapAll(categoryList, CategoryDTO.class);
		
		for (CategoryDTO itemDTO : categoryDTOList) {
			for (Category item : categoryList) {
				if (itemDTO.getId() == item.getId()) {
					itemDTO.setNumberOfProduct(item.getProducts().size());
				}
			}
		}
		
		log.info("######## End get all Category by name: [" + name + "] ########");
		return categoryDTOList;
	}

	@Override
	public Boolean isExistByName(String name) {
		return categoryRepository.existsByName(name);
	}

	@Override
	public Boolean isExistByNameAndIdNot(String name, Integer id) {
		return categoryRepository.existsByNameAndIdNot(name, id);
	}

	@Override
	public Integer count() {
		return (int) categoryRepository.count();
	}

	@Override
	public Integer countByName(String name) {
		return categoryRepository.countByName(name);
	}

	@Override
	public List<CategoryDTO> findAll() {
		List<Category> categoryList = categoryRepository.findAll();
		return ObjectMapperUtils.mapAll(categoryList, CategoryDTO.class);
	}
}
