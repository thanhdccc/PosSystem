package com.fabbi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
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
	public String index(@PathVariable(value = "pageNo") int pageNo, Model model, HttpSession session) {
		int pageSize = Constant.PAGE_SIZE;
		int tmp = orderService.count();
		int totalPage = 0;
		
		if (session.getAttribute(Constant.SESSION_ORDER_INFOR) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_INFOR);
		}
		
		if (session.getAttribute(Constant.SESSION_NAME_EDIT) != null) {
			session.removeAttribute(Constant.SESSION_NAME_EDIT);
		}
		
		if (session.getAttribute(Constant.SESSION_NAME_CREATE) != null) {
			session.removeAttribute(Constant.SESSION_NAME_CREATE);
		}
		
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
	@GetMapping("/orders/edit-order/{id}")
	public String getEditForm(@PathVariable Integer id, Model model, HttpSession session) {

		List<CustomerDTO> customerList = customerService.findAll();
		List<ProductDTO> productList = productService.findAll();
		List<OrderDetailDTO> itemDTOList;
		OrderDTO orderDTO = null;
		
		if (session.getAttribute(Constant.SESSION_ORDER_INFOR) == null) {
			orderDTO = orderService.findOneById(id);
			session.setAttribute(Constant.SESSION_ORDER_INFOR, orderDTO);
		}
		
		orderDTO = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
		
		if (session.getAttribute(Constant.SESSION_NAME_EDIT) != null) {
			
			itemDTOList = (List<OrderDetailDTO>) session.getAttribute(Constant.SESSION_NAME_EDIT);
		} else {
			
			itemDTOList = orderDTO.getItems();
		}
		
		session.setAttribute(Constant.SESSION_NAME_EDIT, itemDTOList);

		model.addAttribute("orderDTO", orderDTO);
		model.addAttribute("customerList", customerList);
		model.addAttribute("productList", productList);
		
		return "edit-order";
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
		
		List<OrderDetailDTO> itemList = (List<OrderDetailDTO>) session.getAttribute(Constant.SESSION_NAME_CREATE);
		
		if (itemList == null) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to saved.");
		} else {
			
			orderDTO.setItems(itemList);
			
			Boolean result = orderService.add(orderDTO);
			
			if (!result) {
				
				redirectAttributes.addFlashAttribute("messageFail", "Failed to saved.");
			} else {
				session.removeAttribute(Constant.SESSION_NAME_CREATE);
				
				redirectAttributes.addFlashAttribute("messageSuccess", "Saved successfully.");
			}
		}
		
		return "redirect:/orders/1";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/orders/edit-order")
	public String editOrder(@Valid OrderDTO orderDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes
			, HttpSession session) {
		
		if (bindingResult.hasErrors()) {
			return "edit-order";
		}
		
		List<OrderDetailDTO> itemDTOList = (List<OrderDetailDTO>) session.getAttribute(Constant.SESSION_NAME_EDIT);
		
		if (itemDTOList == null) {
			
			redirectAttributes.addFlashAttribute("messageFail", "Failed to update.");
		} else {
			
			OrderDTO orderDTOTmp = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
			orderDTO.setId(orderDTOTmp.getId());
			orderDTO.setItems(itemDTOList);
			
			Boolean result = orderService.update(orderDTO);
			
			if (!result) {
				
				redirectAttributes.addFlashAttribute("messageFail", "Failed to update.");
			} else {
				session.removeAttribute(Constant.SESSION_NAME_CREATE);
				
				redirectAttributes.addFlashAttribute("messageSuccess", "Updated successfully.");
			}
		}
		
		return "redirect:/orders/1";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/add-item/{form}/{id}")
	public String addItem(HttpSession session, @PathVariable("id") Integer id, @PathVariable("form") Integer form) {
		
		List<OrderDetailDTO> itemList;
		OrderDTO orderDTO = null;
		
		if (form == Constant.ORDER_FORM_EDIT) {
			orderDTO = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
		}
		
		String sessionName = form == Constant.ORDER_FORM_CREATE ? Constant.SESSION_NAME_CREATE : Constant.SESSION_NAME_EDIT;
		
		if (session.getAttribute(sessionName) == null) {
			itemList = new ArrayList<>();
			ProductDTO productDTO = productService.getById(id);
			
			OrderDetailDTO itemDTO = new OrderDetailDTO();
			itemDTO.setProductId(productDTO.getId());
			itemDTO.setName(productDTO.getName());
			itemDTO.setQuantity(1);
			itemDTO.setThumbnail(productDTO.getThumbnail());
			
			if (orderDTO != null) {
				itemDTO.setOrderId(orderDTO.getId());
			}
			
			itemList.add(itemDTO);
			
			session.setAttribute(sessionName, itemList);
			
		} else {
			itemList = (List<OrderDetailDTO>) session.getAttribute(sessionName);
			OrderDetailDTO tmp = itemList.stream().filter(o -> o.getProductId() == id).findAny().orElse(null);
			
			if (tmp != null) {
				
				Integer quantity = tmp.getQuantity() + 1;
				tmp.setQuantity(quantity);
				
				itemList.remove(tmp);
				itemList.add(tmp);
			} else {
				ProductDTO productDTO = productService.getById(id);
				
				OrderDetailDTO itemDTO = new OrderDetailDTO();
				itemDTO.setProductId(productDTO.getId());
				itemDTO.setName(productDTO.getName());
				itemDTO.setQuantity(1);
				itemDTO.setThumbnail(productDTO.getThumbnail());
				
				if (orderDTO != null) {
					itemDTO.setOrderId(orderDTO.getId());
				}
				itemDTO.setQuantity(1);
				
				itemList.add(itemDTO);
			}
			
			session.removeAttribute(sessionName);
			session.setAttribute(sessionName, itemList);
		}
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order" : "redirect:/orders/edit-order/" + orderDTO.getId();
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/reduce-item/{form}/{id}")
	public String reduceItem(HttpSession session, @PathVariable("id") Integer id, @PathVariable("form") Integer form) {
		
		OrderDTO orderDTO = null;
		
		if (form == Constant.ORDER_FORM_EDIT) {
			orderDTO = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
		}
		
		String sessionName = form == Constant.ORDER_FORM_CREATE ? Constant.SESSION_NAME_CREATE : Constant.SESSION_NAME_EDIT;
		
		List<OrderDetailDTO> itemList = (List<OrderDetailDTO>) session.getAttribute(sessionName);
		OrderDetailDTO tmp = itemList.stream().filter(o -> o.getProductId() == id).findAny().orElse(null);

		Integer quantity = tmp.getQuantity() - 1;

		if (quantity == 0) {
			itemList.remove(tmp);
		} else {
			tmp.setQuantity(quantity);

			itemList.remove(tmp);
			itemList.add(tmp);
		}

		session.removeAttribute(sessionName);
		session.setAttribute(sessionName, itemList);
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order" : "redirect:/orders/edit-order/" + orderDTO.getId();
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/remove-item/{form}/{id}")
	public String removeItem(HttpSession session, @PathVariable("id") Integer id, @PathVariable("form") Integer form) {
		
		OrderDTO orderDTO = null;
		
		if (form == Constant.ORDER_FORM_EDIT) {
			orderDTO = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
		}
		
		String sessionName = form == Constant.ORDER_FORM_CREATE ? Constant.SESSION_NAME_CREATE : Constant.SESSION_NAME_EDIT;
		
		List<OrderDetailDTO> itemList = (List<OrderDetailDTO>) session.getAttribute(sessionName);
		OrderDetailDTO tmp = itemList.stream().filter(o -> o.getProductId() == id).findAny().orElse(null);
		
		itemList.remove(tmp);

		session.removeAttribute(sessionName);
		session.setAttribute(sessionName, itemList);
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order" : "redirect:/orders/edit-order/" + orderDTO.getId();
	}
	
	@GetMapping("/orders/delete-order/{id}")
	public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		log.warn("---- User: [" + userDetail.getUsername() + "] begin delete order with id: [" + id +"]");
		
		Boolean result = orderService.delete(id);
		
		if (!result) {
			redirectAttributes.addFlashAttribute("messageFail", "Failed to delete.");
		} else {
			redirectAttributes.addFlashAttribute("messageSuccess", "Deleted successfully.");
		}
		
		log.warn("---- User: [" + userDetail.getUsername() + "] delete order with id: [" + id +"] successfully.");
		
		return "redirect:/orders/1";
	}
}
