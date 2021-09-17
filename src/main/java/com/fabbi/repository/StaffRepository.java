package com.fabbi.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabbi.entity.User;

@Repository
public interface StaffRepository extends JpaRepository<User, Integer> {

	User findOneById(Integer id);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String username);
    
    Boolean existsByEmailAndIdNot(String username, Integer id);
    
    List<User> findAllBy(Pageable pageable);
    
    @Query(value = "SELECT * FROM user u WHERE u.username LIKE %:keyword%"
    		+ " OR u.fullname LIKE %:keyword%"
    		+ " OR u.address LIKE %:keyword%"
    		+ " OR u.email LIKE %:keyword%", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query(value = "SELECT COUNT(*) FROM user u WHERE u.username LIKE %:keyword%"
    		+ " OR u.fullname LIKE %:keyword%"
    		+ " OR u.address LIKE %:keyword%"
    		+ " OR u.email LIKE %:keyword%", nativeQuery = true)
    Integer countByKeyword(@Param("keyword") String keyword);
}
