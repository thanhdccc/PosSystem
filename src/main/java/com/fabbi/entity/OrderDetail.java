package com.fabbi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orderdetail")
@Getter
@Setter
public class OrderDetail extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1052535215822894442L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "ORDER_DETAIL_ORD_FK"))
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "ORDER_DETAIL_PROD_FK"))
	private Product product;
	
	@Column
	private String thumbnail;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "price", nullable = false)
	private Float price;
	
	@Column(name = "amount", nullable = false)
	private Float amount;
	
	@Column(name = "unit", nullable = false)
	private String unit;
}
