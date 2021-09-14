package com.fabbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fabbi.entity.User;
import com.fabbi.repository.UserRepository;
import com.fabbi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    String ROLE_PREFIX = "ROLE_";

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

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
}