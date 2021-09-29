package com.fabbi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8740663939357056984L;

	@Column
	private String name;
	
	@Column
	private Integer quantity;
	
	@Column
	private Integer price;
	
	@Column
	private String thumbnail;
	
	@Column
	private Integer unit;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column
	private String note;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
	private Supplier supplier;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
	private Category category;
	
	@Version
	private Long version;
}
