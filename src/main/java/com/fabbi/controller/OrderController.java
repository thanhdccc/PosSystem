package com.fabbi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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
import com.fabbi.dto.OrderDetailDTO;
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
	
	@SuppressWarnings("unchecked")
	@PostMapping("/orders/add-order")
	public String addOrder(@Valid OrderDTO orderDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes
			, HttpSession session) {
		
		if (bindingResult.hasErrors()) {
			return "add-order";
		}
		
		List<ProductDTO> itemList = (List<ProductDTO>) session.getAttribute("order");
		List<OrderDetailDTO> itemDTOList = new ArrayList<>();
		
		if (itemList == null) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to saved.");
		} else {
			
			for (ProductDTO productDTO : itemList) {
				OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
				orderDetailDTO.setProductId(productDTO.getId());
				orderDetailDTO.setQuantity(productDTO.getQuantity());
				
				itemDTOList.add(orderDetailDTO);
			}
			
			orderDTO.setItems(itemDTOList);
			
			Boolean result = orderService.add(orderDTO);
			
			if (!result) {
				
				redirectAttributes.addFlashAttribute("messageFail", "Failed to saved.");
			} else {
				session.removeAttribute("order");
				
				redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
			}
		}
		
		return "redirect:/orders/1";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/add-item/{id}")
	public String addItem(HttpSession session, @PathVariable("id") Integer id) {
		
		List<ProductDTO> itemList;
		
		if (session.getAttribute("order") == null) {
			itemList = new ArrayList<>();
			ProductDTO itemDTO = productService.getById(id);
			itemDTO.setQuantity(1);
			itemList.add(itemDTO);
			
			session.setAttribute("order", itemList);
		} else {
			itemList = (List<ProductDTO>) session.getAttribute("order");
			ProductDTO itemDTO = productService.getById(id);
			ProductDTO tmp = itemList.stream().filter(o -> o.getId() == itemDTO.getId()).findAny().orElse(null);
			
			if (tmp != null) {
				
				Integer quantity = tmp.getQuantity() + 1;
				tmp.setQuantity(quantity);
				
				itemList.remove(tmp);
				itemList.add(tmp);
			} else {
				itemDTO.setQuantity(1);
				itemList.add(itemDTO);
			}
			
			session.removeAttribute("order");
			session.setAttribute("order", itemList);
		}
		return "redirect:/orders/add-order";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/reduce-item/{id}")
	public String reduceItem(HttpSession session, @PathVariable("id") Integer id) {
		
		List<ProductDTO> itemList = (List<ProductDTO>) session.getAttribute("order");
		ProductDTO tmp = itemList.stream().filter(o -> o.getId() == id).findAny().orElse(null);

		Integer quantity = tmp.getQuantity() - 1;

		if (quantity == 0) {
			itemList.remove(tmp);
		} else {
			tmp.setQuantity(quantity);

			itemList.remove(tmp);
			itemList.add(tmp);
		}

		session.removeAttribute("order");
		session.setAttribute("order", itemList);

		return "redirect:/orders/add-order";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/remove-item/{id}")
	public String removeItem(HttpSession session, @PathVariable("id") Integer id) {
		
		List<ProductDTO> itemList = (List<ProductDTO>) session.getAttribute("order");
		ProductDTO tmp = itemList.stream().filter(o -> o.getId() == id).findAny().orElse(null);
		
		itemList.remove(tmp);

		session.removeAttribute("order");
		session.setAttribute("order", itemList);
		
		return "redirect:/orders/add-order";
	}
}