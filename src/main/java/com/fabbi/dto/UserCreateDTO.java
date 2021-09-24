package com.fabbi.dto;

import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class UserCreateDTO {
	
	@Id
	private Integer id;

	@NotBlank(message = "Username is mandatory")
	@Size(min = 8, max = 20, message = "Username must be between 8 and 20")
	private String username;

	@NotBlank(message = "Password is mandatory")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20")
	private String password;
	
	@NotBlank(message = "Fullname is mandatory")
	@Size(min = 6, max = 100, message = "Fullname must be between 6 and 100")
	private String fullname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	@NotNull(message = "Gender is mandatory")
	private Integer gender;
	
	@NotBlank(message = "Address is mandatory")
	private String address;
	
	@Email(message = "Email is wrong format")
	@NotBlank(message = "Email is mandatory")
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "Phone number is wrong format")
	@NotBlank(message = "Phone is mandatory")
	private String phone;
	
	@NotNull(message = "Type is mandatory")
	private Integer type;
	
	private Boolean isActive;
	
	private String role;
	
	private String errorField;
}
