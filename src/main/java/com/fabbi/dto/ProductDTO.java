package com.fabbi.dto;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

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
	
	@NotNull(message = "Quantity is mandatory")
	@Range(min = 1, max = 1000)
	private Integer quantity;
	
	@NotNull(message = "Price is mandatory")
	@Range(min = 1000, max = 999999999)
	private Integer price;

	private String thumbnail;
	
	@NotNull(message = "Unit is mandatory")
	private Integer unit;
	
	@NotNull(message = "Supplier is mandatory")
	private Integer supplierId;
	
	@NotNull(message = "Category is mandatory")
	private Integer categoryId;

	private String supplierName;

	private String categoryName;

	private String description;
	
	private String note;
	
	private String errorField;
	
	@Transient
	public String getThumbnailImagePath() {
		if (thumbnail == null || id == null) {
			return null;
		}
		
		return "/product-images/" + thumbnail;
	}
}
