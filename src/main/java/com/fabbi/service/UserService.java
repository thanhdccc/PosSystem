package com.fabbi.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.fabbi.dto.UserDTO;

public interface UserService extends UserDetailsService {
	
	Boolean add(UserDTO userDTO);
	
	Boolean update(UserDTO userDTO);
	
	UserDTO getById(Integer id);
	
	List<UserDTO> findPaginated(int pageNo, int pageSize);
	
	Boolean delete(Integer id);
	
	Boolean isUserExist(UserDTO userDTO);
	
	List<UserDTO> search(String username, int pageNo, int pageSize);
}
