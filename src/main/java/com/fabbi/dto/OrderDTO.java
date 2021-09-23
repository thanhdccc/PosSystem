package com.fabbi.dto;

import java.util.List;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

	@Id
	private Integer id;
	
	private Integer status;

	private Integer customerId;
	
	private String customerName;
	
	private Integer amount;
	
	private Integer numberOfItem;
	
	private List<OrderDetailDTO> items;
}
