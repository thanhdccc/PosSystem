package com.fabbi.dto;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

	@Id
	private Integer id;
	
	@Pattern(regexp = "^(?:\\d{10}|)$", message = "Phone number is wrong format")
	private String phone;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 6, max = 100, message = "Fullname must be between 6 and 100")
	private String name;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	@NotBlank(message = "Gender is mandatory")
	private String gender;

	private String address;
	
	@Pattern(regexp = "(^$|^.*@.*\\..*$)", message = "Email in not correct format")
	private String email;

	@NotBlank(message = "Type is mandatory")
	private String type;
	
	private String note;
	
	private String errorField;
}
