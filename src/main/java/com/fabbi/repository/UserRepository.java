package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
    User findByUsername(String username);
    
    User findOneById(Integer id);
    
    Boolean existsByUsernameOrEmail(String username, String email);
    
    List<User> getAllUser(Pageable pageable);
    
    List<User> findAllByUsernameLike(String username, Pageable pageable);
}
