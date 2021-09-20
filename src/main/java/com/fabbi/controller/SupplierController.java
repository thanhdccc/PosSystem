package com.fabbi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;

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
}
