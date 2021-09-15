package com.fabbi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findOneByName(String name);
}
