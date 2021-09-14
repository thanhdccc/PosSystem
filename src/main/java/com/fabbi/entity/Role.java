package com.fabbi.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role extends Base {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 431747385147568722L;
	
	@Column(length = 20, unique = true)
	private String name;
	
	@OneToMany(mappedBy = "role")
    private Set<User> users;

	public Role() {
	}

	public Role(String name, Set<User> users) {
		this.name = name;
		this.users = users;
	}
}
