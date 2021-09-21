package com.fabbi.service.impl;

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
	private ProductRepository productRepositoty;
	
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
		
		Integer unit = productDTO.getUnit() == "K" ? Constant.UNIT_KILOGRAM : Constant.UNIT_PIECE;
		
		Product product = ObjectMapperUtils.map(productDTO, Product.class);
		product.setQuantity(Integer.parseInt(productDTO.getQuantity()));
		product.setPrice(Integer.parseInt(productDTO.getPrice()));
		product.setCategory(category);
		product.setSupplier(supplier);
		product.setUnit(unit);
		
		try {
			productRepositoty.save(product);
		} catch (Exception e) {
			log.error("Cannot insert Product: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert Product ########");
		return true;
	}

	@Override
	public Boolean isExistByCategoryId(Integer id) {
		return productRepositoty.existsByCategoryId(id);
	}

	@Override
	public Boolean isExistBySupplierId(Integer id) {
		return productRepositoty.existsBySupplierId(id);
	}

	@Override
	public Boolean isExistByNameAndCategoryId(String name, Integer id) {
		return productRepositoty.existsByNameAndCategoryId(name, id);
	}

	@Override
	public Boolean isExistByNameAndSupplierId(String name, Integer id) {
		return productRepositoty.existsByNameAndSupplierId(name, id);
	}

	@Override
	public List<ProductDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Product ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Product> productList = productRepositoty.findAllBy(pageable);
		List<ProductDTO> productDTOList = new ArrayList<>();
		
		for (Product item : productList) {
			ProductDTO itemDTO = ObjectMapperUtils.map(item, ProductDTO.class);
			itemDTO.setSupplierName(item.getSupplier().getName());
			itemDTO.setCategoryName(item.getCategory().getName());
			itemDTO.setQuantity(item.getQuantity().toString());
			itemDTO.setPrice(item.getPrice().toString());
			itemDTO.setUnit(item.getUnit() == Constant.UNIT_KILOGRAM ? "Kilogram" : "Piece");
			productDTOList.add(itemDTO);
		}
		
		log.info("######## End get all Product ########");
		return productDTOList;
	}

	@Override
	public Integer count() {
		return (int) productRepositoty.count();
	}
}
