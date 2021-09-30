package com.fabbi.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CustomerDTO;
import com.fabbi.dto.OrderCustomerDTO;
import com.fabbi.service.CustomerService;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerAPI {
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/phone-validate", method = RequestMethod.POST)
	public String validatePhone(String phone, HttpSession session) {
		
		CustomerDTO result = customerService.getByPhone(phone);
		
		if (result != null) {
			
			if (session.getAttribute(Constant.SESSION_CUSTOMER_INFOR) != null) {
				session.removeAttribute(Constant.SESSION_CUSTOMER_INFOR);
			}
			session.setAttribute(Constant.SESSION_CUSTOMER_INFOR, result);
			
			return "Error";
		}
		return "Success";
	}
	
	@RequestMapping(value = "/order-add-customer", method = RequestMethod.POST)
	public String addCustomerFromOrder(String name, String email, Integer gender, String phone, String address, HttpSession session) {
		OrderCustomerDTO customerDTO = new OrderCustomerDTO(phone, name, gender, address, email, Constant.CUSTOMER_TYPE_NEW);
		
		CustomerDTO result = customerService.addCustomerFromOrder(customerDTO);
		
		if (result != null) {
			
			if (session.getAttribute(Constant.SESSION_CUSTOMER_INFOR) != null) {
				session.removeAttribute(Constant.SESSION_CUSTOMER_INFOR);
			}
			session.setAttribute(Constant.SESSION_CUSTOMER_INFOR, result);
			
			return "Success";
		}
		
		return "Error";
	}
}
