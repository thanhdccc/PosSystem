package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.dto.UserCreateDTO;
import com.fabbi.dto.UserUpdateDTO;
import com.fabbi.entity.Role;
import com.fabbi.entity.User;
import com.fabbi.repository.RoleRepository;
import com.fabbi.repository.StaffRepository;
import com.fabbi.service.StaffService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StaffServiceImpl implements StaffService {
	
	@Autowired
    private StaffRepository staffRepository;
    
	@Autowired
    private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Boolean add(UserCreateDTO userDTO) {
		log.info("######## Begin insert User ########");
		
		User userEntity = ObjectMapperUtils.map(userDTO, User.class);
		Role role = roleRepository.findOneByName("USER");
		
		if (role == null) {
			log.error("Cannot insert User because role is not exist");
			return false;
		}
		
		userEntity.setIsActive(true);
		userEntity.setRole(role);
		userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		try {
			staffRepository.save(userEntity);
		} catch (Exception e) {
			log.error("Cannot insert User: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert User ########");
		return true;
	}

	@Override
	public Boolean update(UserUpdateDTO userDTO) {
		log.info("######## Begin update User ########");
		
		User userEntity = staffRepository.findOneById(userDTO.getId());
		
		if (userEntity == null) {
			log.error("User with id: [" + userDTO.getId() + "] not exist");
			return false;
		}
		
		User userNew = ObjectMapperUtils.map(userDTO, User.class);
		userNew.setRole(userEntity.getRole());
		userNew.setId(userEntity.getId());
		userNew.setPassword(userEntity.getPassword());
		userNew.setUsername(userEntity.getUsername());
		userNew.setIsActive(true);
		
		try {
			staffRepository.save(userNew);
		} catch (Exception e) {
			log.error("Cannot update User: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public UserCreateDTO getById(Integer id) {
		log.info("######## Begin get User by ID ########");
		
		User userEntity = staffRepository.findOneById(id);
		
		if (userEntity == null) {
			log.error("User with id: [" + id + "] not exist");
			return null;
		}
		
		UserCreateDTO userDTO = ObjectMapperUtils.map(userEntity, UserCreateDTO.class);
		userDTO.setRole(userEntity.getRole().getName());
		
		log.info("######## End get User by ID ########");
		return userDTO;
	}

	@Override
	public List<UserCreateDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all User ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<User> userList = staffRepository.findAllBy(pageable);
		List<UserCreateDTO> userDTOList = new ArrayList<>();
		
		for (User user : userList) {
			UserCreateDTO userDTO = ObjectMapperUtils.map(user, UserCreateDTO.class);
			userDTO.setRole(user.getRole().getName());
			userDTOList.add(userDTO);
		}
		
		log.info("######## End get all User ########");
		return userDTOList;
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete User by ID ########");
		
		User userEntity = staffRepository.findOneById(id);
		
		if (userEntity == null) {
			log.error("User with id: [" + id + "] not exist");
			return false;
		}
		
		userEntity.setIsActive(false);
		
		try {
			staffRepository.save(userEntity);
		} catch (Exception e) {
			log.error("Cannot deactive User: " + e.getMessage());
			return false;
		}
		
		log.info("######## End get User by ID ########");
		return true;
	}
	
	@Override
	public List<UserCreateDTO> search(String keyword, int pageNo, int pageSize) {
		log.info("######## Begin search User by keyword: [" + keyword + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<User> userList = staffRepository.findByKeyword(keyword, pageable);
		List<UserCreateDTO> userDTOList = new ArrayList<>();
		
		for (User user : userList) {
			UserCreateDTO userDTO = ObjectMapperUtils.map(user, UserCreateDTO.class);
			userDTO.setRole(user.getRole().getName());
			userDTOList.add(userDTO);
		}
		
		log.info("######## Begin search User by keyword: [" + keyword + "] ########");
		return userDTOList;
	}

	@Override
	public Boolean isUserExistByUsername(String username) {
		Boolean result = staffRepository.existsByUsername(username);
		return result;
	}
	
	@Override
	public Boolean isUserExistByEmail(String email) {
		Boolean result = staffRepository.existsByEmail(email);
		return result;
	}

	@Override
	public Boolean isUserExistByEmailAndIdNot(String email, Integer id) {
		Boolean result = staffRepository.existsByEmailAndIdNot(email, id);
		return result;
	}

	@Override
	public Integer count() {
		return (int) staffRepository.count();
	}

	@Override
	public Integer countByKeyword(String keyword) {
		return staffRepository.countByKeyword(keyword);
	}
}
