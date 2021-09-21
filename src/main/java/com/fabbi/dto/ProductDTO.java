package com.fabbi.dto;

import javax.persistence.Id;
import javax.persistence.Transient;
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
	
	@Pattern(regexp = "^\\d{1,5}$", message = "Input integer only")
	@NotBlank(message = "Quantity is mandatory")
	private String quantity;
	
	@Pattern(regexp = "^\\d{1,8}$", message = "Input integer only")
	@NotBlank(message = "Price is mandatory")
	private String price;

	private String thumbnail;
	
	@NotBlank(message = "Unit is mandatory")
	private String unit;
	
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
		
		return "/product-images/" + "supplier-" + supplierId + "/" + thumbnail;
	}
}
