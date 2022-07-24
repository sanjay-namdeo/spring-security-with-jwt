package com.learn.security.springsecuritywithjwt.service;

import com.learn.security.springsecuritywithjwt.entity.UserEntity;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import com.learn.security.springsecuritywithjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return user.map(userEntity -> new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>())).orElse(null);
    }
}
