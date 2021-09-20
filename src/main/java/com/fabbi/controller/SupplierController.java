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
import com.fabbi.dto.SupplierDTO;
import com.fabbi.service.SupplierService;

@Controller
public class SupplierController {

	@Autowired
	private SupplierService supplierService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/suppliers/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = supplierService.count();
		int totalPage = 0;
		
		List<SupplierDTO> supplierList = supplierService.findPaginated(pageNo, pageSize);
		
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
		model.addAttribute("totalItems", supplierList.size());
		model.addAttribute("supplierList", supplierList);
		
		return "list-supplier";
	}
	
	@GetMapping("/suppliers/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = 0;
		int totalPage = 0;
		
		List<SupplierDTO> supplierList;
		
		if (keyword != null) {
			tmp = supplierService.countByKeyword(keyword);
			supplierList = supplierService.search(keyword, pageNo, pageSize);
			
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
			tmp = supplierService.count();
			supplierList = supplierService.findPaginated(pageNo, pageSize);
			
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
		model.addAttribute("totalItems", supplierList.size());
		model.addAttribute("supplierList", supplierList);
		model.addAttribute("keyword", keyword);
		
		return "list-supplier";
	}
	
	@GetMapping("/suppliers/add-supplier")
	public String getAddForm(@ModelAttribute SupplierDTO supplierDTO, Model model) {
		model.addAttribute("supplierDTO", supplierDTO);
		return "add-supplier";
	}
	
	@PostMapping("/suppliers/add-supplier")
	public String addSupplier(@Valid SupplierDTO supplierDTO, BindingResult bindingResult) {

		Boolean isExistByEmail = supplierService.isExistByEmail(supplierDTO.getEmail());
		if (isExistByEmail) {
			bindingResult.addError(new FieldError("supplierDTO", "email", "Email already in use!"));
			return "add-supplier";
		}
		
		Boolean isExistByPhone = supplierService.isExistByPhone(supplierDTO.getPhone());
		if (isExistByPhone) {
			bindingResult.addError(new FieldError("supplierDTO", "phone", "Phone already in use!"));
			return "add-supplier";
		}
		
		if (bindingResult.hasErrors()) {
			return "add-supplier";
		}
		
		Boolean result = supplierService.add(supplierDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("supplierDTO", "errorField", "Error to create Supplier"));
			return "add-supplier";
		}
		
		return "redirect:/suppliers/1";
	}
	
	@GetMapping("/suppliers/edit-supplier/{id}")
	public String getEditForm(@PathVariable Integer id, Model model) {
		SupplierDTO supplierDTO = supplierService.getById(id);
		model.addAttribute("supplierDTO", supplierDTO);
		return "edit-supplier";
	}
	
	@PostMapping("/suppliers/edit-supplier")
	public String updateSupplier(@Valid SupplierDTO supplierDTO, BindingResult bindingResult) {

		Boolean isExistByEmail = supplierService.isExistByEmailAndIdNot(supplierDTO.getEmail(), supplierDTO.getId());
		if (isExistByEmail) {
			bindingResult.addError(new FieldError("supplierDTO", "email", "Email already in use!"));
			return "edit-supplier";
		}
		
		Boolean isExistByPhone = supplierService.isExistByPhoneAndIdNot(supplierDTO.getPhone(), supplierDTO.getId());
		if (isExistByPhone) {
			bindingResult.addError(new FieldError("supplierDTO", "phone", "Phone already in use!"));
			return "edit-supplier";
		}
		
		if (bindingResult.hasErrors()) {
			return "edit-supplier";
		}
		
		Boolean result = supplierService.update(supplierDTO);
		
		if (!result) {
			bindingResult.addError(new FieldError("supplierDTO", "errorField", "Error to update Supplier"));
			return "edit-supplier";
		}
		
		return "redirect:/suppliers/1";
	}
}
