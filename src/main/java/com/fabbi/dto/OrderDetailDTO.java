package com.fabbi.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

	@Id
	private Integer id;
	
	@NotNull(message = "Order is mandatory")
	private Integer orderId;
	
	@NotNull(message = "Product is mandatory")
	private Integer productId;
	
	private String thumbnail;
	
	@NotNull(message = "Quantity is mandatory")
	private Integer quantity;
	
	private Integer price;
	
	private Integer amount;

	private Integer unit;
}
