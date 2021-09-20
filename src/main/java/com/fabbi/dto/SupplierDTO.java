package com.fabbi.dto;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {

	@Id
	private Integer id;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 6, max = 100, message = "Fullname must be between 6 and 100")
	private String name;
	
	@Pattern(regexp = "^\\d{10}$", message = "Phone number is wrong format")
	@NotBlank(message = "Phone is mandatory")
	private String phone;
	
	@NotBlank(message = "Address is mandatory")
	private String address;

	@NotBlank(message = "City is mandatory")
	private String city;
	
	@Email(message = "Email in not correct format")
	@NotBlank(message = "Email is mandatory")
	private String email;

	private String description;
	
	private String note;
	
	private String errorField;
}
