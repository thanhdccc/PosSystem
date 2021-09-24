package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CustomerDTO;
import com.fabbi.entity.Customer;
import com.fabbi.repository.CustomerRepository;
import com.fabbi.service.CustomerService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Boolean add(CustomerDTO customer) {
		log.info("######## Begin insert Customer ########");
		
		Customer customerEntity = ObjectMapperUtils.map(customer, Customer.class);
		
		customerEntity.setType(Constant.CUSTOMER_TYPE_NEW);
		customerEntity.setIsDeleted(false);
		
		try {
			customerRepository.save(customerEntity);
		} catch (Exception e) {
			log.error("Cannot insert Customer: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert Customer ########");
		return true;
	}

	@Override
	public Boolean update(CustomerDTO customer) {
		log.info("######## Begin update Customer ########");
		
		Customer oldCustomer = customerRepository.findOneById(customer.getId());
		
		if (oldCustomer == null) {
			log.error("Customer with id: [" + customer.getId() + "] not exist");
			return false;
		}
		
		Customer newCustomer = ObjectMapperUtils.map(customer, Customer.class);
		newCustomer.setId(oldCustomer.getId());
		newCustomer.setIsDeleted(oldCustomer.getIsDeleted());
		
		try {
			customerRepository.save(newCustomer);
		} catch (Exception e) {
			log.error("Cannot insert Customer: " + e.getMessage());
			return false;
		}
		
		log.info("######## End update Customer ########");
		return true;
	}

	@Override
	public CustomerDTO getById(Integer id) {
		log.info("######## Begin get Customer by ID ########");
		
		Customer customer = customerRepository.findOneById(id);
		
		if (customer == null) {
			log.error("Customer with id: [" + id + "] not exist");
			return null;
		}

		CustomerDTO customerDTO = ObjectMapperUtils.map(customer, CustomerDTO.class);
		
		log.info("######## End get Customer by ID ########");
		return customerDTO;
	}

	@Override
	public List<CustomerDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Customer ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Customer> customerList = customerRepository.findAllBy(pageable);
		List<CustomerDTO> customerDTOList = new ArrayList<>();
		
		for (Customer item : customerList) {
			CustomerDTO itemDTO = ObjectMapperUtils.map(item, CustomerDTO.class);
			customerDTOList.add(itemDTO);
		}
		
		log.info("######## End get all Customer ########");
		return customerDTOList;
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete Customer ########");
		
		Customer customer = customerRepository.findOneById(id);
		
		if (customer == null) {
			log.error("Customer with id: [" + id + "] not exist");
			return false;
		}
		
		customer.setIsDeleted(true);
		
		try {
			customerRepository.save(customer);
		} catch (Exception e) {
			log.error("Cannot delete Customer: " + e.getMessage());
			return false;
		}
		
		log.info("######## End delete Customer ########");
		return true;
	}

	@Override
	public List<CustomerDTO> search(String keyword, int pageNo, int pageSize) {
		log.info("######## Begin search Customer by keyword: [" + keyword + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Customer> customerList = customerRepository.findByKeyword(keyword, pageable);
		List<CustomerDTO> customerDTOList = new ArrayList<>();
		
		for (Customer item : customerList) {
			CustomerDTO itemDTO = ObjectMapperUtils.map(item, CustomerDTO.class);
			customerDTOList.add(itemDTO);
		}
		
		log.info("######## End search Customer by keyword: [" + keyword + "] ########");
		return customerDTOList;
	}

	@Override
	public Boolean isExistByEmail(String email) {
		return customerRepository.existsByEmail(email);
	}

	@Override
	public Boolean isExistByPhone(String phone) {
		return customerRepository.existsByPhone(phone);
	}

	@Override
	public Boolean isExistByPhoneAndIdNot(String phone, Integer id) {
		return customerRepository.existsByPhoneAndIdNot(phone, id);
	}

	@Override
	public Integer count() {
		return (int) customerRepository.count();
	}

	@Override
	public Integer countByKeyword(String keyword) {
		return customerRepository.countByKeyword(keyword);
	}

	@Override
	public Boolean isExistByEmailAndIdNot(String email, Integer id) {
		return customerRepository.existsByEmailAndIdNot(email, id);
	}

	@Override
	public List<CustomerDTO> findAll() {
		List<Customer> customerList = customerRepository.findAll();
		return ObjectMapperUtils.mapAll(customerList, CustomerDTO.class);
	}
}
