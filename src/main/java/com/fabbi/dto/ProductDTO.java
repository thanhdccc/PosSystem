package com.fabbi.dto;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class ProductDTO {

	@Id
	private Integer id;
	
	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 100, message = "Fullname must be between 2 and 100")
	private String name;
	
	@Pattern(regexp = "^\\d{5}$", message = "Input integer only")
	@NotBlank(message = "Quantity is mandatory")
	private String quantity;
	
	@Pattern(regexp = "^\\d{8}$", message = "Input integer only")
	@NotBlank(message = "Price is mandatory")
	private String price;
	
	@NotBlank(message = "Image is mandatory")
	private String thumbnail;
	
	@NotBlank(message = "Unit is mandatory")
	private String unit;
	
	@NotNull(message = "Supplier is mandatory")
	private Integer supplierId;
	
	@NotNull(message = "Category is mandatory")
	private Integer categoryId;

	private String description;
	
	private String note;
	
	private String errorField;
}
