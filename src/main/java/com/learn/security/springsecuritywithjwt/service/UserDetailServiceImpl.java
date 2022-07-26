package com.learn.security.springsecuritywithjwt.service;

import com.learn.security.springsecuritywithjwt.entity.UserEntity;
import com.learn.security.springsecuritywithjwt.exception.UserNotFoundException;
import com.learn.security.springsecuritywithjwt.pojo.SignupRequest;
import com.learn.security.springsecuritywithjwt.pojo.SignupResponse;
import com.learn.security.springsecuritywithjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    public SignupResponse saveUser(SignupRequest signupRequest) {
        UserEntity userEntity = new UserEntity(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getEmail(), signupRequest.getName(), signupRequest.getMobile());
        UserEntity savedUser = this.userRepository.save(userEntity);
        return SignupResponse.build(savedUser.getName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getMobile());
    }
}
