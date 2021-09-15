package com.fabbi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer", uniqueConstraints = {@UniqueConstraint(columnNames = "phone")})
@Getter
@Setter
public class Customer extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4698436937212198914L;

	@Column(name = "phone", length = 20)
	private String phone;
	
	@Column(name = "name")
	private String name;
	
	@Column
	private Date dob;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "type")
	private String type;
	
	@Column
	private String note;
}
