package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.dto.UserDTO;
import com.fabbi.entity.Role;
import com.fabbi.entity.User;
import com.fabbi.repository.RoleRepository;
import com.fabbi.repository.UserRepository;
import com.fabbi.service.UserService;
import com.fabbi.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    String ROLE_PREFIX = "ROLE_";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
            user.getPassword(), list);
    }

	@Override
	public Boolean add(UserDTO userDTO) {
		log.info("######## Begin insert User ########");
		
		User userEntity = ObjectMapperUtils.map(userDTO, User.class);
		Role role = roleRepository.findOneByName(ROLE_PREFIX + "USER");
		
		if (role == null) {
			log.error("Cannot insert User because role is not exist");
			return false;
		}
		
		userEntity.setIsActive(true);
		userEntity.setRole(role);
		
		try {
			userRepository.save(userEntity);
		} catch (Exception e) {
			log.error("Cannot insert User: " + e.getMessage());
			return false;
		}
		
		log.info("######## End insert User ########");
		return true;
	}

	@Override
	public Boolean update(UserDTO userDTO) {
		log.info("######## Begin update User ########");
		
		User userEntity = userRepository.findOneById(userDTO.getId());
		
		if (userEntity == null) {
			log.error("User with id: [" + userDTO.getId() + "] not exist");
			return false;
		}
		
		User userNew = ObjectMapperUtils.map(userDTO, User.class);
		userNew.setRole(userEntity.getRole());
		userNew.setId(userEntity.getId());
		
		try {
			userRepository.save(userNew);
		} catch (Exception e) {
			log.error("Cannot update User: " + e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public UserDTO getById(Integer id) {
		log.info("######## Begin get User by ID ########");
		
		User userEntity = userRepository.findOneById(id);
		
		if (userEntity == null) {
			log.error("User with id: [" + id + "] not exist");
			return null;
		}
		
		UserDTO userDTO = ObjectMapperUtils.map(userEntity, UserDTO.class);
		userDTO.setIsActive(userEntity.getIsActive() == true ? "Active" : "Inactive");
		userDTO.setRole(userEntity.getRole().getName());
		
		log.info("######## End get User by ID ########");
		return userDTO;
	}

	@Override
	public List<UserDTO> findPaginated(int pageNo, int pageSize) {
		log.info("######## Begin get all User ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<User> userList = userRepository.getAllUser(pageable);
		List<UserDTO> userDTOList = new ArrayList<>();
		
		for (User user : userList) {
			UserDTO userDTO = ObjectMapperUtils.map(user, UserDTO.class);
			userDTO.setRole(user.getRole().getName());
			userDTO.setIsActive(user.getIsActive() == true ? "Active" : "Inactive");
			userDTOList.add(userDTO);
		}
		
		log.info("######## End get all User ########");
		return userDTOList;
	}

	@Override
	public Boolean delete(Integer id) {
		log.info("######## Begin delete User by ID ########");
		
		User userEntity = userRepository.findOneById(id);
		
		if (userEntity == null) {
			log.error("User with id: [" + id + "] not exist");
			return false;
		}
		
		userEntity.setIsActive(false);
		
		try {
			userRepository.save(userEntity);
		} catch (Exception e) {
			log.error("Cannot deactive User: " + e.getMessage());
			return false;
		}
		
		log.info("######## End get User by ID ########");
		return true;
	}
	
	@Override
	public List<UserDTO> search(String username, int pageNo, int pageSize) {
		log.info("######## Begin search all User by name like: [" + username + "] ########");
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		List<User> userList = userRepository.findAllByUsernameLike(username, pageable);
		List<UserDTO> userDTOList = new ArrayList<>();
		
		for (User user : userList) {
			UserDTO userDTO = ObjectMapperUtils.map(user, UserDTO.class);
			userDTO.setRole(user.getRole().getName());
			userDTO.setIsActive(user.getIsActive() == true ? "Active" : "Inactive");
			userDTOList.add(userDTO);
		}
		
		log.info("######## Begin search all User by name like: [" + username + "] ########");
		return userDTOList;
	}

	@Override
	public Boolean isUserExist(UserDTO userDTO) {
		Boolean result = userRepository.existsByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail());
		return result;
	}
}