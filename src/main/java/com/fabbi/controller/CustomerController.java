package com.fabbi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CustomerDTO;
import com.fabbi.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/customers/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = customerService.count();
		int totalPage = 0;
		
		List<CustomerDTO> customerList = customerService.findPaginated(pageNo, pageSize);
		
		if (tmp < pageSize) {
			totalPage = 1;
		} else {
			if (tmp % pageSize == 0) {
				totalPage = tmp / pageSize;
			} else {
				totalPage = (tmp / pageSize) + 1;
			}
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", customerList.size());
		model.addAttribute("customerList", customerList);
		
		return "list-customer";
	}
	
	@GetMapping("/customers/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = 0;
		int totalPage = 0;
		
		List<CustomerDTO> customerList;
		
		if (keyword != null) {
			tmp = customerService.countByKeyword(keyword);
			customerList = customerService.search(keyword, pageNo, pageSize);
			
			if (tmp < pageSize) {
				totalPage = 1;
			} else {
				if (tmp % pageSize == 0) {
					totalPage = tmp / pageSize;
				} else {
					totalPage = (tmp / pageSize) + 1;
				}
			}
			
		} else {
			tmp = customerService.count();
			customerList = customerService.findPaginated(pageNo, pageSize);
			
			if (tmp < pageSize) {
				totalPage = 1;
			} else {
				if (tmp % pageSize == 0) {
					totalPage = tmp / pageSize;
				} else {
					totalPage = (tmp / pageSize) + 1;
				}
			}
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", customerList.size());
		model.addAttribute("customerList", customerList);
		model.addAttribute("keyword", keyword);
		
		return "list-customer";
	}
	
	@GetMapping("/customers/add-customer")
	public String getAddForm(@ModelAttribute CustomerDTO customerDTO, Model model) {
		model.addAttribute("customerDTO", customerDTO);
		return "add-customer";
	}
	
	@PostMapping("/customers/add-customer")
	public String addCustomer(@Valid CustomerDTO customerDTO, BindingResult bindingResult) {
		
		if (customerDTO.getEmail() != null) {
			Boolean isExistByEmail= customerService.isExistByEmail(customerDTO.getEmail());
			if (isExistByEmail) {
				bindingResult.addError(new FieldError("customerDTO", "email", "Email already in use!"));
			}
		}
		
		Boolean isExistByPhone = customerService.isExistByPhone(customerDTO.getPhone());
		if (isExistByPhone) {
			bindingResult.addError(new FieldError("customerDTO", "phone", "Phone number already in use!"));
		}
		
		if (bindingResult.hasErrors()) {
			return "add-customer";
		}
		
		Boolean result = customerService.add(customerDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("customerDTO", "errorField", "Error to create User"));
			return "add-customer";
		}
		
		return "redirect:/customers/1";
	}
	
	@GetMapping("/customers/edit-customer/{id}")
	public String getEditForm(@PathVariable Integer id, Model model) {
		CustomerDTO customerDTO = customerService.getById(id);
		model.addAttribute("customerDTO", customerDTO);
		return "edit-customer";
	}
	
	@PostMapping("/customers/edit-customer")
	public String updateCustomer(@Valid CustomerDTO customerDTO, BindingResult bindingResult) {
		
		if (customerDTO.getEmail() != null) {
			Boolean isExistByEmailAndIdNot= customerService.isExistByEmailAndIdNot(customerDTO.getEmail(), customerDTO.getId());
			if (isExistByEmailAndIdNot) {
				bindingResult.addError(new FieldError("customerDTO", "email", "Email already in use!"));
			}
		}
		
		Boolean isExistByPhoneAndIdNot = customerService.isExistByPhoneAndIdNot(customerDTO.getPhone(), customerDTO.getId());
		if (isExistByPhoneAndIdNot) {
			bindingResult.addError(new FieldError("customerDTO", "phone", "Phone number already in use!"));
		}
		
		if (bindingResult.hasErrors()) {
			return "add-customer";
		}
		
		Boolean result = customerService.update(customerDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("customerDTO", "errorField", "Error to update User"));
			return "add-customer";
		}
		
		return "redirect:/customers/1";
	}
	
	@GetMapping("/customers/delete-customer/{id}")
	public String deleteCustomer(@PathVariable Integer id) {
		
		Boolean result = customerService.delete(id);
		
		if (!result) {
			
		}
		
		return "redirect:/customers/1";
	}
}
