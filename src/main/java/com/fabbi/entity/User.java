package com.fabbi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Getter
@Setter
public class User extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2029885763717425144L;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	@Column(name = "fullname")
	private String fullname;
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dob;
	
	@Column
	private Integer gender;
	
	@Column(name = "email")
	private String email;
	
	@Column
	private String address;
	
	@Column(name = "phone", length = 20)
	private String phone;
	
	@Column
	private Integer type;
	
	@Column
	private Boolean isActive;

	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
	
	public User() {
		
	}

	public User(String userName, String password) {
		this.username = userName;
		this.password = password;
	}
	
	public User(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(String username, String password, String fullname, Date dob, String address, String phone,
			Boolean isActive, Role role) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.dob = dob;
		this.address = address;
		this.phone = phone;
		this.isActive = isActive;
		this.role = role;
	}
}
