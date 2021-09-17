package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		
		customerEntity.setType("N");
		
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
		
		String type = customer.getType().equals("N") ? "New" : customer.getType().equals("V") ? "Vip" : "Deleted";
		String gender = customer.getGender().equals("M") ? "Male" : "Female";

		CustomerDTO customerDTO = ObjectMapperUtils.map(customer, CustomerDTO.class);
		customerDTO.setType(type);
		customer.setGender(gender);
		
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
			String type = item.getType().equals("N") ? "New" : item.getType().equals("V") ? "Vip" : "Deleted";
			String gender = item.getGender().equals("M") ? "Male" : "Female";
			CustomerDTO itemDTO = ObjectMapperUtils.map(item, CustomerDTO.class);
			itemDTO.setType(type);
			itemDTO.setGender(gender);
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
		
		customer.setType("D");
		
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
		log.info("######## Begin search User by keyword: [" + keyword + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Customer> customerList = customerRepository.findByKeyword(keyword, pageable);
		List<CustomerDTO> customerDTOList = new ArrayList<>();
		
		for (Customer item : customerList) {
			String type = item.getType().equals("N") ? "New" : item.getType().equals("V") ? "Vip" : "Deleted";
			String gender = item.getGender().equals("M") ? "Male" : "Female";
			CustomerDTO itemDTO = ObjectMapperUtils.map(item, CustomerDTO.class);
			itemDTO.setType(type);
			itemDTO.setGender(gender);
			customerDTOList.add(itemDTO);
		}
		
		log.info("######## End search User by keyword: [" + keyword + "] ########");
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
}
