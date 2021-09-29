package com.fabbi.dto;

import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCustomerDTO {

	@Id
	private Integer id;

	private String phone;
	
	private String name;
	
	private Integer gender;

	private String address;

	private String email;

	private Integer type;

	public OrderCustomerDTO(String phone, String name, Integer gender, String address, String email, Integer type) {
		this.phone = phone;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.email = email;
		this.type = type;
	}
}
