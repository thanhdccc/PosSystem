package com.fabbi.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.constant.Constant;
import com.fabbi.dto.ProductDTO;
import com.fabbi.entity.Category;
import com.fabbi.entity.Product;
import com.fabbi.entity.Supplier;
import com.fabbi.repository.CategoryRepository;
import com.fabbi.repository.ProductRepository;
import com.fabbi.repository.SupplierRepository;
import com.fabbi.service.ProductService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepositoty;
	
	@Autowired
	private SupplierRepository supplierRepository;

	@Override
	public Boolean add(ProductDTO productDTO) {
		log.info("######## Begin insert Product ########");
		
		Category category = categoryRepositoty.findOneById(productDTO.getCategoryId());
		
		if (category == null) {
			log.error("Category with id: [" + productDTO.getCategoryId() + "] not exist");
			return false;
		}
		
		Supplier supplier = supplierRepository.findOneById(productDTO.getSupplierId());
		
		if (supplier == null) {
			log.error("Supplier with id: [" + productDTO.getSupplierId() + "] not exist");
			return false;
		}
		
		Product product = ObjectMapperUtils.map(productDTO, Product.class);

		product.setCategory(category);
		product.setSupplier(supplier);
		
		if (productDTO.getThumbnail().equals("")) {
			product.setThumbnail("");
		}
		
		try {
			productRepository.save(product);
		} catch (Exception e) {
			log.error("Cannot insert Product: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert Product ########");
		return true;
	}

	@Override
	public Boolean isExistByCategoryId(Integer id) {
		return productRepository.existsByCategoryId(id);
	}

	@Override
	public Boolean isExistBySupplierId(Integer id) {
		return productRepository.existsBySupplierId(id);
	}

	@Override
	public Boolean isExistByNameAndCategoryId(String name, Integer id) {
		return productRepository.existsByNameAndCategoryId(name, id);
	}

	@Override
	public Boolean isExistByNameAndSupplierId(String name, Integer id) {
		return productRepository.existsByNameAndSupplierId(name, id);
	}

	@Override
	public List<ProductDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Product ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Product> productList = productRepository.findAllBy(pageable);
		List<ProductDTO> productDTOList = new ArrayList<>();
		
		for (Product item : productList) {
			ProductDTO itemDTO = ObjectMapperUtils.map(item, ProductDTO.class);
			itemDTO.setSupplierName(item.getSupplier().getName());
			itemDTO.setCategoryName(item.getCategory().getName());
			productDTOList.add(itemDTO);
		}
		
		log.info("######## End get all Product ########");
		return productDTOList;
	}

	@Override
	public Integer count() {
		return (int) productRepository.count();
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete Product ########");
		
		Product product = productRepository.findOneById(id);
		
		if (product == null) {
			log.error("Product with id: [" + id + "] not exist.");
			return false;
		}
		
		String thumbnail = product.getThumbnail();
		
		try {
			productRepository.delete(product);
			log.info("Success to remove image");
		} catch (Exception e) {
			log.error("Failed to remove product with id [" + id + "]: " + e.getMessage());
			return false;
		}
		
		Path path = Paths.get(Constant.UPLOAD_DIR + "/" + thumbnail);
		
		try {
			Files.delete(path);
		} catch (IOException e) {
			log.warn("Failed to remove image: [" + e.getMessage() + "]");
		}
		
		log.info("######## End get Product ########");
		
		return true;
	}

	@Override
	public ProductDTO getById(Integer id) {
		log.info("######## Begin get Product by ID ########");
		
		Product product = productRepository.findOneById(id);
		
		if (product == null) {
			log.error("Product with id: [" + id + "] not exist.");
			return null;
		}
		
		ProductDTO productDTO = ObjectMapperUtils.map(product, ProductDTO.class);
		productDTO.setSupplierName(product.getSupplier().getName());
		productDTO.setCategoryName(product.getCategory().getName());
		productDTO.setSupplierId(product.getSupplier().getId());
		productDTO.setCategoryId(product.getCategory().getId());
		
		log.info("######## End get Product by ID ########");
		
		return productDTO;
	}

	@Override
	public Boolean update(ProductDTO productDTO) {
		log.info("######## Begin update Product ########");
		
		Integer id = productDTO.getId();
		
		try {
			Product oldProduct = productRepository.findOneById(id);
			
			if (oldProduct == null) {
				log.error("Product with id: [" + id + "] not exist.");
				return false;
			}
			
			String oldThumbnail = oldProduct.getThumbnail();
			Long version = oldProduct.getVersion();
			
			Product newProduct = ObjectMapperUtils.map(productDTO, Product.class);
			
			Category category = categoryRepositoty.findOneById(productDTO.getCategoryId());
			
			if (category == null) {
				log.error("Category with id: [" + productDTO.getCategoryId() + "] not exist");
				return false;
			}
			
			Supplier supplier = supplierRepository.findOneById(productDTO.getSupplierId());
			
			if (supplier == null) {
				log.error("Supplier with id: [" + productDTO.getSupplierId() + "] not exist");
				return false;
			}
			
			if (productDTO.getThumbnail().equals("")) {
				newProduct.setThumbnail(oldThumbnail);
			}
			
			newProduct.setSupplier(supplier);
			newProduct.setCategory(category);
			newProduct.setId(oldProduct.getId());
			newProduct.setVersion(version++);
			
			try {
				productRepository.save(newProduct);
			} catch (Exception e) {
				log.error("Cannot update Product: " + e.getMessage());
				return false;
			}
			
			if (!productDTO.getThumbnail().equals("")) {
				
				Path path = Paths.get(Constant.UPLOAD_DIR + "/" + oldThumbnail);
				
				try {
					Files.delete(path);
				} catch (IOException e) {
					log.warn("Failed to remove image: [" + e.getMessage() + "]");
				}
			}
		} catch (Exception e) {
			log.error("------ Product information had been changed");
			return false;
		}
		
		log.info("######## End update Product ########");
		
		return true;
	}

	@Override
	public List<ProductDTO> search(String keyword, int pageNo, int pageSize) {
		log.info("######## Begin get all Product by keyword ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Product> productList = productRepository.findByKeyword(keyword, pageable);
		List<ProductDTO> productDTOList = new ArrayList<>();
		
		for (Product item : productList) {
			ProductDTO itemDTO = ObjectMapperUtils.map(item, ProductDTO.class);
			itemDTO.setSupplierName(item.getSupplier().getName());
			itemDTO.setCategoryName(item.getCategory().getName());
			productDTOList.add(itemDTO);
		}
		
		log.info("######## End get all Product by keyword ########");
		return productDTOList;
	}

	@Override
	public Integer countByKeyword(String keyword) {
		return productRepository.countByKeyword(keyword);
	}

	@Override
	public Boolean isExistByNameAndCategoryIdAndIdNot(String name, Integer categoryId, Integer id) {
		return productRepository.existsByNameAndCategoryIdAndIdNot(name, categoryId, id);
	}

	@Override
	public Boolean isExistByNameAndSupplierIdAndIdNot(String name, Integer supplierId, Integer id) {
		return productRepository.existsByNameAndSupplierIdAndIdNot(name, supplierId, id);
	}

	@Override
	public List<ProductDTO> findAll() {
		List<Product> productList = productRepository.findAll();
		List<ProductDTO> productDTOList = new ArrayList<>();
		
		for (Product item: productList) {
			ProductDTO itemDTO = ObjectMapperUtils.map(item, ProductDTO.class);
			itemDTO.setCategoryName(item.getCategory().getName());
			itemDTO.setSupplierName(item.getSupplier().getName());
			productDTOList.add(itemDTO);
		}
		
		return productDTOList;
	}
}
