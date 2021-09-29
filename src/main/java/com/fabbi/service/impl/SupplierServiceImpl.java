package com.fabbi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.dto.SupplierDTO;
import com.fabbi.entity.Supplier;
import com.fabbi.repository.ProductRepository;
import com.fabbi.repository.SupplierRepository;
import com.fabbi.service.SupplierService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SupplierServiceImpl implements SupplierService {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Boolean add(SupplierDTO supplierDTO) {
		log.info("######## Begin insert Supplier ########");
		
		Supplier supplier = ObjectMapperUtils.map(supplierDTO, Supplier.class);
		
		try {
			supplierRepository.save(supplier);
		} catch (Exception e) {
			log.error("Cannot insert Supplier: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert Supplier ########");
		return true;
	}

	@Override
	public Boolean update(SupplierDTO supplierDTO) {
		log.info("######## Begin update Supplier ########");
		
		Supplier oldSupplier = supplierRepository.findOneById(supplierDTO.getId());
		
		if (oldSupplier == null) {
			log.error("Supplier with id: [" + supplierDTO.getId() + "] not exist");
			return false;
		}
		
		Supplier newSupplier = ObjectMapperUtils.map(supplierDTO, Supplier.class);
		newSupplier.setId(oldSupplier.getId());
		
		try {
			supplierRepository.save(newSupplier);
		} catch (Exception e) {
			log.error("Cannot update Supplier: " + e.getMessage());
			return false;
		}
		
		log.info("######## End update Supplier ########");
		return true;
	}

	@Override
	public SupplierDTO getById(Integer id) {
		log.info("######## Begin get Supplier by ID ########");
		
		Supplier supplier = supplierRepository.findOneById(id);
		
		if (supplier == null) {
			log.error("Supplier with id: [" + id + "] not exist");
			return null;
		}
		
		SupplierDTO supplierDTO = ObjectMapperUtils.map(supplier, SupplierDTO.class);
		
		log.info("######## End get Supplier by ID ########");
		return supplierDTO;
	}

	@Override
	public List<SupplierDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Supplier ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Supplier> supplierList = supplierRepository.findAllBy(pageable);
		List<SupplierDTO> supplierDTOList = ObjectMapperUtils.mapAll(supplierList, SupplierDTO.class);
		
		log.info("######## End get all Supplier ########");
		
		return supplierDTOList;
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete Supplier by ID: [" + id + "] ########");
		
		Supplier supplier = supplierRepository.findOneById(id);
		
		if (supplier == null) {
			log.error("Supplier with id: [" + id + "] not exist");
			return null;
		}
		
		if (productRepository.countBySupplierId(id) >= 1) {
			log.error("Delete Supplier with id: [" + id + "] failed because there is product belong to.");
			return false;
		}
		
		try {
			supplierRepository.delete(supplier);
		} catch (Exception e) {
			log.error("Cannot delete Supplier: " + e.getMessage());
			return false;
		}
		
		log.info("######## End delete Supplier by ID: [" + id + "] ########");
		return true;
	}

	@Override
	public List<SupplierDTO> search(String keyword, int pageNo, int pageSize) {
		log.info("######## Begin search Supplier by keyword: [" + keyword + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Supplier> supplierList = supplierRepository.findByKeyword(keyword, pageable);
		List<SupplierDTO> supplierDTOList = ObjectMapperUtils.mapAll(supplierList, SupplierDTO.class);
		
		log.info("######## End search Supplier by keyword: [" + keyword + "] ########");
		
		return supplierDTOList;
	}

	@Override
	public Boolean isExistByEmail(String email) {
		return supplierRepository.existsByEmail(email);
	}

	@Override
	public Boolean isExistByPhone(String phone) {
		return supplierRepository.existsByPhone(phone);
	}

	@Override
	public Boolean isExistByPhoneAndIdNot(String phone, Integer id) {
		return supplierRepository.existsByPhoneAndIdNot(phone, id);
	}

	@Override
	public Boolean isExistByEmailAndIdNot(String email, Integer id) {
		return supplierRepository.existsByEmailAndIdNot(email, id);
	}

	@Override
	public Integer count() {
		return (int) supplierRepository.count();
	}

	@Override
	public Integer countByKeyword(String keyword) {
		return supplierRepository.countByKeyword(keyword);
	}

	@Override
	public List<SupplierDTO> findAll() {
		List<Supplier> supplierList = supplierRepository.findAll();
		return ObjectMapperUtils.mapAll(supplierList, SupplierDTO.class);
	}
}
