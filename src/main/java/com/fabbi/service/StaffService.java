package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.UserCreateDTO;
import com.fabbi.dto.UserUpdateDTO;

public interface StaffService {

	Boolean add(UserCreateDTO userDTO);
	
	Boolean update(UserUpdateDTO userDTO);
	
	UserCreateDTO getById(Integer id);
	
	List<UserCreateDTO> findPaginated(int pageNo, int pageSize);
	
	Boolean delete(Integer id);
	
	Boolean isUserExistByUsername(String username);
	
	Boolean isUserExistByEmail(String email);
	
	Boolean isUserExistByEmailAndIdNot(String email, Integer id);
	
	List<UserCreateDTO> search(String keyword, int pageNo, int pageSize);
	
	Integer count();
	
	Integer countByKeyword(String keyword);
}
