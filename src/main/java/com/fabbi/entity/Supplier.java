package com.fabbi.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@Getter
@Setter
public class Supplier extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5863485774365542278L;

	@Column
	private String name;
	
	@Column
	private String phone;
	
	@Column
	private String address;
	
	@Column
	private String city;
	
	@Column
	private String fax;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column
	private String note;
	
	@OneToMany(mappedBy = "supplier")
	private List<Product> products = new ArrayList<>();
}
