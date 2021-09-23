package com.fabbi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fabbi.constant.Constant;
import com.fabbi.dto.CustomerDTO;
import com.fabbi.dto.OrderDTO;
import com.fabbi.dto.ProductDTO;
import com.fabbi.service.CustomerService;
import com.fabbi.service.OrderService;
import com.fabbi.service.ProductService;

@Controller
public class OrderController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/orders/{pageNo}")
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = orderService.count();
		int totalPage = 0;
		
		List<OrderDTO> orderList = orderService.findPaginated(pageNo, pageSize);
		
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
		model.addAttribute("totalItems", orderList.size());
		model.addAttribute("orderList", orderList);
		
		return "list-order";
	}

	@GetMapping("/orders/add-order")
	public String getAddForm(@ModelAttribute OrderDTO orderDTO, Model model) {
		List<CustomerDTO> customerList = customerService.findAll();
		List<ProductDTO> productList = productService.findAll();
		
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		
		return "add-order";
	}
	
	@PostMapping("/orders/add-order")
	public String addOrder(@Valid OrderDTO orderDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			return "add-order";
		}
		
		Boolean result = orderService.add(orderDTO);
		
		if (!result) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to saved.");
		} else {
			
			redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
		}
		
		return "redirect:/orders/1";
	}
}
