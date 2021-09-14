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
@Table(name = "category")
@Getter
@Setter
public class Category extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5827181122414095683L;
	
	@Column
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;

	@OneToMany(mappedBy = "category")
	private List<Product> products = new ArrayList<>();
}
