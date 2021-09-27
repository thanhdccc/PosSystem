package com.fabbi.dto;

import javax.persistence.Id;
import javax.persistence.Transient;
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
	
	private String name;
	
	private String thumbnail;
	
	@NotNull(message = "Quantity is mandatory")
	private Integer quantity;
	
	private Integer price;
	
	private Integer amount;

	private Integer unit;
	
	@Transient
	public String getThumbnailImagePath() {
		if (thumbnail == null) {
			return null;
		}
		
		return "/product-images/" + thumbnail;
	}
}
