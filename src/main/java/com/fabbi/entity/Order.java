package com.fabbi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private Integer amount;
	
	@Column(name = "status")
	private Integer status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = true, foreignKey = @ForeignKey(name = "ORDER_CUS_FK"))
	private Customer customer;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> items = new ArrayList<>();
}
