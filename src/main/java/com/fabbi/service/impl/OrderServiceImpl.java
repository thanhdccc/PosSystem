package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.constant.Constant;
import com.fabbi.dto.OrderDTO;
import com.fabbi.dto.OrderDetailDTO;
import com.fabbi.entity.Customer;
import com.fabbi.entity.Order;
import com.fabbi.entity.OrderDetail;
import com.fabbi.entity.Product;
import com.fabbi.repository.CustomerRepository;
import com.fabbi.repository.OrderDetailRepository;
import com.fabbi.repository.OrderRepository;
import com.fabbi.repository.ProductRepository;
import com.fabbi.service.OrderService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Boolean add(OrderDTO orderDTO) {
		Integer totalAmount = 0;
		List<OrderDetail> itemList = new ArrayList<>();
		
		log.info("######## Begin insert Order ########");
		
		Order order = ObjectMapperUtils.map(orderDTO, Order.class);
		Customer customer = null;
		
		if (orderDTO.getCustomerId() != null) {
			customer = customerRepository.findOneById(orderDTO.getCustomerId());
		}
		
		order.setCustomer(customer);
		
		List<OrderDetailDTO> itemDTOList = orderDTO.getItems();
		
		if (itemDTOList.size() ==  0) {
			log.error("No product found in order");
			return false;
		}
		
		for (OrderDetailDTO itemDTO: itemDTOList) {
			
			try {
				Product product = productRepository.findOneById(itemDTO.getProductId());
				
				Integer stockQuantity = product.getQuantity();
				
				if (stockQuantity - itemDTO.getQuantity() < 0) {
					log.error("Product with id: [" + itemDTO.getProductId() + "] quantity out of stock.");
					return false;
				}
				
				itemDTO.setThumbnail(product.getThumbnail());
				itemDTO.setUnit(product.getUnit());
				itemDTO.setPrice(product.getPrice());
				itemDTO.setAmount(itemDTO.getQuantity() * itemDTO.getPrice());
				
				totalAmount += itemDTO.getAmount();
				
				OrderDetail item = ObjectMapperUtils.map(itemDTO, OrderDetail.class);
				item.setOrder(order);
				item.setProduct(product);
				
				itemList.add(item);
				
				product.setQuantity(stockQuantity - itemDTO.getQuantity());
				
				try {
					log.info("Begin update product quantity: [" + product.getName() + "]");
					productRepository.save(product);
				} catch (Exception e) {
					log.error("Error to update product quantity in stock...");
					return false;
				}
				
			} catch (Exception e) {
				log.error("------ Product information had been changed...");
				return false;
			}
		}
		
		order.setItems(itemList);
		order.setAmount(totalAmount);
		order.setStatus(Constant.ORDER_STATUS_WAIT);
		
		try {
			orderRepository.save(order);
		} catch (Exception e) {
			log.error("Error to save order...");
			return false;
		}
		
		for (OrderDetail item : itemList) {
			try {
				orderDetailRepository.save(item);
			} catch (Exception e) {
				log.error("Error to save order item...");
				return false;
			}
		}
		
		log.info("######## End insert Order ########");
		
		return true;
	}

	@Override
	public List<OrderDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all Order ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<Order> orderList = orderRepository.findAllBy(pageable);
		List<OrderDTO> orderDTOList = new ArrayList<>();
		
		for (Order item : orderList) {
			OrderDTO itemDTO = ObjectMapperUtils.map(item, OrderDTO.class);
			itemDTO.setNumberOfItem(item.getItems().size());
			
			if (item.getCustomer() != null) {
				itemDTO.setCustomerName(item.getCustomer().getName());
			} else {
				itemDTO.setCustomerName("Unknown");
			}
			
			orderDTOList.add(itemDTO);
		}
		
		log.info("######## End get all Order ########");
		
		return orderDTOList;
	}

	@Override
	public Integer count() {
		return (int) orderRepository.count();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Boolean update(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDTO findOneById(Integer id) {
		log.info("######## Begin get Order by ID ########");
		
		Order order = orderRepository.findOneById(id);

		if (order == null) {
			log.error("Order with id: [" + id + "] not exist");
			return null;
		}
		
		List<OrderDetail> orderDetailList = order.getItems();
		List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
		
		for (OrderDetail item : orderDetailList) {
			OrderDetailDTO itemDTO = ObjectMapperUtils.map(item, OrderDetailDTO.class);
			itemDTO.setProductId(item.getProduct().getId());
			itemDTO.setOrderId(item.getOrder().getId());
			itemDTO.setThumbnail(item.getThumbnail());
			itemDTO.setName(item.getProduct().getName());
			
			orderDetailDTOList.add(itemDTO);
		}
		
		OrderDTO orderDTO = ObjectMapperUtils.map(order, OrderDTO.class);
		orderDTO.setItems(orderDetailDTOList);
		if (order.getCustomer() != null) {
			orderDTO.setCustomerId(order.getCustomer().getId());
		}
		
		return orderDTO;
	}

}
