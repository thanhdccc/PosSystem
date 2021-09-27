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
		Integer totalAmount = 0;
		List<OrderDetail> oldItemList = new ArrayList<>();
		List<OrderDetail> newItemListChange = new ArrayList<>();
		List<OrderDetailDTO> newItemList = new ArrayList<>();
		
		log.info("######## Begin update Order ########");
		
		Integer orderID = orderDTO.getId();
		
		Order oldOrder = orderRepository.findOneById(orderID);
		
		if (oldOrder == null) {
			log.error("Order with id: [" + orderID + "] not exist.");
			return false;
		}
		
		if (oldOrder.getStatus() == Constant.ORDER_STATUS_PROCESS) {
			log.error("Cannot update order with status is [Process].");
			return false;
		}
		
		Order newOrder = ObjectMapperUtils.map(orderDTO, Order.class);
		Customer customer = null;
		
		if (orderDTO.getCustomerId() != null) {
			customer = customerRepository.findOneById(orderDTO.getCustomerId());
		}
		
		oldItemList = orderDetailRepository.findAllByOrderId(orderID);
		newItemList = orderDTO.getItems();
		
		for (int i = 0; i < newItemList.size(); i++) {
			
			try {
				OrderDetailDTO itemDTO = newItemList.get(i);
				
				Product product = productRepository.findOneById(itemDTO.getProductId());
				
				Integer stockQuantity = product.getQuantity();
				Integer changeQuantity = 0;
				
				//find item exist in both old and new
				OrderDetail itemTmp = oldItemList.stream()
						.filter(o -> o.getProduct().getId() == itemDTO.getProductId())
						.findAny()
						.orElse(null);
				
				if (itemTmp != null) {
					oldItemList.remove(itemTmp);
				}
				
				if (itemTmp != null) {
					
					changeQuantity = stockQuantity + itemTmp.getQuantity() - itemDTO.getQuantity();
					
					if (changeQuantity < 0) {
						log.error("Product with id: [" + itemDTO.getProductId() + "] quantity out of stock.");
						return false;
					}
					
					itemDTO.setAmount(itemDTO.getQuantity() * itemTmp.getPrice());
					
					totalAmount += itemDTO.getAmount();
					
					itemTmp.setQuantity(itemDTO.getQuantity());
					itemTmp.setAmount(itemDTO.getAmount());
					
					newItemListChange.add(itemTmp);
					
					product.setQuantity(changeQuantity);
					
					try {
						log.info("Begin update product quantity: [" + product.getName() + "]");
						productRepository.save(product);
					} catch (Exception e) {
						log.error("Error to update product quantity in stock...");
						return false;
					}
					
				} else {
					
					changeQuantity = stockQuantity - itemDTO.getQuantity();
					
					if (changeQuantity < 0) {
						log.error("Product with id: [" + itemDTO.getProductId() + "] quantity out of stock.");
						return false;
					}
					
					itemDTO.setUnit(product.getUnit());
					itemDTO.setPrice(product.getPrice());
					itemDTO.setAmount(itemDTO.getQuantity() * itemDTO.getPrice());
					
					totalAmount += itemDTO.getAmount();
					
					OrderDetail orderDetail = ObjectMapperUtils.map(itemDTO, OrderDetail.class);
					orderDetail.setOrder(oldOrder);
					orderDetail.setProduct(product);
					
					newItemListChange.add(orderDetail);
					
					product.setQuantity(changeQuantity);
					
					try {
						log.info("Begin update product quantity: [" + product.getName() + "]");
						productRepository.save(product);
					} catch (Exception e) {
						log.error("Error to update product quantity in stock...");
						return false;
					}
				}
				
				//return product quantity if edited order remove item in old order
				if (i == (newItemList.size() - 1)) {
					for (OrderDetail item : oldItemList) {
						Product productTmp = productRepository.findOneById(item.getProduct().getId());
						
						Integer stockQuantityTmp = productTmp.getQuantity();
						
						productTmp.setQuantity(stockQuantityTmp + item.getQuantity());
						
						try {
							log.info("Begin update product quantity: [" + product.getName() + "]");
							productRepository.save(product);
						} catch (Exception e) {
							log.error("Error to update product quantity in stock...");
							return false;
						}
					}
				}
				
			} catch (Exception e) {
				log.error("------ Product information had been changed...");
				return false;
			}
		}
		
		newOrder.setId(orderID);
		newOrder.setItems(newItemListChange);
		newOrder.setAmount(totalAmount);
		
		if (customer != null) {
			newOrder.setCustomer(customer);
		}
		
		try {
			orderRepository.save(newOrder);
		} catch (Exception e) {
			log.error("Error to save order...");
			return false;
		}
		
		for (OrderDetail item : newItemListChange) {
			try {
				orderDetailRepository.save(item);
			} catch (Exception e) {
				log.error("Error to save item in order...");
				return false;
			}
		}
		
		for (OrderDetail item : oldItemList) {
			try {
				orderDetailRepository.delete(item);
			} catch (Exception e) {
				log.error("Error to delete item in order...");
				return false;
			}
		}
		
		log.info("######## End update Order ########");
		
		return true;
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

	@Override
	public List<OrderDTO> search(String keyword, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete Order ########");
		
		Order order = orderRepository.findOneById(id);
		
		if (order == null) {
			log.error("Order with id: [" + id + "] not exist.");
			return false;
		}
		
		if (order.getStatus() == Constant.ORDER_STATUS_PROCESS) {
			log.error("Cannot update order with status is [Process].");
			return false;
		}
		
		List<OrderDetail> itemList = orderDetailRepository.findAllByOrderId(order.getId());
		
		for (OrderDetail item : itemList) {
			try {
				Product product = productRepository.findOneById(item.getProduct().getId());
				
				Integer changeQuantity = product.getQuantity() + item.getQuantity();
				
				product.setQuantity(changeQuantity);
				
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
			
			try {
				orderDetailRepository.deleteById(item.getId());
			} catch (Exception e) {
				log.error("Error to remove item [" + item.getId() + "] in order");
				return false;
			}
		}
		
		try {
			orderRepository.deleteById(id);
		} catch (Exception e) {
			log.error("Error to remove order with id: [" + id + "]");
			return false;
		}
		
		log.info("######## End delete Order ########");
		
		return true;
	}

}
