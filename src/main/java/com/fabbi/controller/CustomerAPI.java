package com.fabbi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.constant.Constant;
import com.fabbi.dto.OrderCustomerDTO;
import com.fabbi.service.CustomerService;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerAPI {
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/phone-validate", method = RequestMethod.POST)
	public String validatePhone(String phone) {
		Boolean result = customerService.isExistByPhone(phone);
		return result == true ? "Error" : "Success";
	}
	
	@RequestMapping(value = "/order-add-customer", method = RequestMethod.POST)
	public String addCustomerFromOrder(String name, String email, Integer gender, String phone, String address) {
		OrderCustomerDTO customerDTO = new OrderCustomerDTO(phone, name, gender, address, email, Constant.CUSTOMER_TYPE_NEW);
		
		Boolean result = customerService.addCustomerFromOrder(customerDTO);
		return result == true ? "Error" : "Success";
	}
}
