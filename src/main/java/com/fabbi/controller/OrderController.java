package com.fabbi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
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
		
		if (session.getAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT);
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
	
	@GetMapping("/orders/search/{pageNo}")
	public String search(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, Model model, HttpSession session) {
		
		String keywordTmp = keyword;
		
		if (keyword != null) {
			if (Constant.ORDER_STATUS_WAIT_TEXT.toLowerCase().contains(keyword.toLowerCase())) {
				
				keyword = String.valueOf(Constant.ORDER_STATUS_WAIT);
			} else if (Constant.ORDER_STATUS_PROCESS_TEXT.toLowerCase().contains(keyword.toLowerCase())) {
				
				keyword = String.valueOf(Constant.ORDER_STATUS_PROCESS);
			} else {
				
				keyword = "999";
			}
		}
		
		int pageSize = Constant.PAGE_SIZE;
		int tmp = 0;
		int totalPage = 0;
		List<OrderDTO> orderList = null;
		
		if (session.getAttribute(Constant.SESSION_ORDER_INFOR) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_INFOR);
		}
		
		if (session.getAttribute(Constant.SESSION_NAME_EDIT) != null) {
			session.removeAttribute(Constant.SESSION_NAME_EDIT);
		}
		
		if (session.getAttribute(Constant.SESSION_NAME_CREATE) != null) {
			session.removeAttribute(Constant.SESSION_NAME_CREATE);
		}
		
		if (keyword != null) {
			tmp = orderService.countByKeyword(Integer.parseInt(keyword));

			orderList = orderService.searchStatus(Integer.parseInt(keyword), pageNo, pageSize);

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
			tmp = orderService.count();
			
			orderList = orderService.findPaginated(pageNo, pageSize);
			
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
		model.addAttribute("totalItems", orderList.size());
		model.addAttribute("orderList", orderList);
		model.addAttribute("keyword", keywordTmp);
		
		return "list-order";
	}

	@GetMapping("/orders/add-order/{pageNo}")
	public String getAddForm(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, @ModelAttribute OrderDTO orderDTO, Model model) {
		int pageSize = 6;
		int tmp = 0;
		int totalPage = 0;
		
		List<ProductDTO> productList = null;

		List<CustomerDTO> customerList = customerService.findAll();
		if (keyword != null) {
			tmp = productService.countByKeyword(keyword);
			
			productList = productService.search(keyword, pageNo, pageSize);
			
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
			tmp = productService.count();
			
			productList = productService.findPaginated(pageNo, pageSize);
			
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
		model.addAttribute("totalItems", productList.size());
		model.addAttribute("productList", productList);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("customerList", customerList);
		
		return "add-order";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/orders/edit-order/{pageNo}/{id}")
	public String getEditForm(@PathVariable(value = "pageNo") int pageNo, @Param("keyword") String keyword, @PathVariable Integer id, Model model, HttpSession session) {
		int pageSize = 6;
		int tmp = 0;
		int totalPage = 0;
		List<ProductDTO> productList = null;
		
		if (keyword != null) {
			tmp = productService.countByKeyword(keyword);
			
			productList = productService.search(keyword, pageNo, pageSize);
			
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
			tmp = productService.count();
			
			productList = productService.findPaginated(pageNo, pageSize);
			
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
		
		List<OrderDetailDTO> itemDTOList;
		OrderDTO orderDTO = null;
		
		if (session.getAttribute(Constant.SESSION_ORDER_INFOR) == null) {
			orderDTO = orderService.findOneById(id);
			session.setAttribute(Constant.SESSION_ORDER_INFOR, orderDTO);
		}
		
		orderDTO = (OrderDTO) session.getAttribute(Constant.SESSION_ORDER_INFOR);
		
		CustomerDTO customerDTO = customerService.getById(orderDTO.getCustomerId());
		
		if (session.getAttribute(Constant.SESSION_NAME_EDIT) != null) {
			
			itemDTOList = (List<OrderDetailDTO>) session.getAttribute(Constant.SESSION_NAME_EDIT);
		} else {
			
			itemDTOList = orderDTO.getItems();
		}
		
		Integer totalAmount = itemDTOList.stream().mapToInt(t -> t.getAmount()).sum();
		
		if (session.getAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT);
		}
		session.setAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT, totalAmount);
		
		session.setAttribute(Constant.SESSION_NAME_EDIT, itemDTOList);

		model.addAttribute("orderDTO", orderDTO);
		if (customerDTO != null) {
			model.addAttribute("customerDTO", customerDTO);
		} else {
			model.addAttribute("customerDTO", null);
		}
		
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("totalItems", productList.size());
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
			orderDTO.setCustomerId(orderDTOTmp.getCustomerId());
			
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
			itemDTO.setPrice(productDTO.getPrice());
			itemDTO.setAmount(itemDTO.getQuantity() * itemDTO.getPrice());
			itemDTO.setThumbnail(productDTO.getThumbnail());
			
			if (orderDTO != null) {
				itemDTO.setOrderId(orderDTO.getId());
			}
			
			itemList.add(itemDTO);
			
			Integer totalAmount = itemList.stream().mapToInt(t -> t.getAmount()).sum();

			session.setAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT, totalAmount);
			session.setAttribute(sessionName, itemList);
			
		} else {
			itemList = (List<OrderDetailDTO>) session.getAttribute(sessionName);
			OrderDetailDTO tmp = itemList.stream().filter(o -> o.getProductId() == id).findAny().orElse(null);
			
			int editItemIndex = 0;
			OrderDetailDTO editItem = null;
			
			if (tmp != null) {
				editItemIndex = itemList.indexOf(tmp);
				editItem = itemList.get(editItemIndex);
			}
			
			if (editItem != null) {
				
				Integer quantity = editItem.getQuantity() + 1;
				editItem.setQuantity(quantity);
				editItem.setAmount(quantity * tmp.getPrice());
				
			} else {
				ProductDTO productDTO = productService.getById(id);
				
				OrderDetailDTO itemDTO = new OrderDetailDTO();
				itemDTO.setProductId(productDTO.getId());
				itemDTO.setName(productDTO.getName());
				itemDTO.setQuantity(1);
				itemDTO.setPrice(productDTO.getPrice());
				itemDTO.setAmount(itemDTO.getQuantity() * itemDTO.getPrice());
				itemDTO.setThumbnail(productDTO.getThumbnail());
				
				if (orderDTO != null) {
					itemDTO.setOrderId(orderDTO.getId());
				}
				itemDTO.setQuantity(1);
				
				itemList.add(itemDTO);
			}
			
			Integer totalAmount = itemList.stream().mapToInt(t -> t.getAmount()).sum();
			
			if (session.getAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT) != null) {
				session.removeAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT);
			}
			session.setAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT, totalAmount);
			
			if (session.getAttribute(sessionName) != null) {
				session.removeAttribute(sessionName);
			}
			session.setAttribute(sessionName, itemList);
		}
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order/1" : "redirect:/orders/edit-order/1/" + orderDTO.getId();
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
		
		int editItemIndex = 0;
		OrderDetailDTO editItem = null;
		
		if (tmp != null) {
			editItemIndex = itemList.indexOf(tmp);
			editItem = itemList.get(editItemIndex);
		}

		Integer quantity = editItem.getQuantity() - 1;

		if (quantity == 0) {
			itemList.remove(editItem);
		} else {
			editItem.setQuantity(quantity);
			editItem.setAmount(quantity * tmp.getPrice());
		}

		Integer totalAmount = itemList.stream().mapToInt(t -> t.getAmount()).sum();
		
		if (session.getAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT);
		}
		session.setAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT, totalAmount);
		
		if (session.getAttribute(sessionName) != null) {
			session.removeAttribute(sessionName);
		}
		session.setAttribute(sessionName, itemList);
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order/1" : "redirect:/orders/edit-order/1/" + orderDTO.getId();
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
		
		Integer totalAmount = itemList.stream().mapToInt(t -> t.getAmount()).sum();
		
		if (session.getAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT) != null) {
			session.removeAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT);
		}
		session.setAttribute(Constant.SESSION_ORDER_TOTAL_AMOUNT, totalAmount);
		
		if (session.getAttribute(sessionName) != null) {
			session.removeAttribute(sessionName);
		}
		session.setAttribute(sessionName, itemList);
		
		return form == Constant.ORDER_FORM_CREATE ? "redirect:/orders/add-order/1" : "redirect:/orders/edit-order/1/" + orderDTO.getId();
	}
	
	@GetMapping("/orders/delete-order/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		
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
