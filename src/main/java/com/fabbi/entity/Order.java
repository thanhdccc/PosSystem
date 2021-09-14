package com.fabbi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7806375938098341255L;

	@Column(name = "amount")
	private Float amount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "customerId")
	private Integer customerId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> items = new ArrayList<>();
}
