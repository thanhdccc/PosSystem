package com.fabbi.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

	@Id
	private Integer id;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 4, max = 100, message = "Name must be between 4 and 100")
	private String name;
	
	private Integer numberOfProduct;
	
	private String description;
	
	private String errorField;
}
