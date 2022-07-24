package com.learn.security.springsecuritywithjwt;

import com.learn.security.springsecuritywithjwt.entity.UserEntity;
import com.learn.security.springsecuritywithjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringSecurityWithJwtApplication {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void UserInit() {
        List<UserEntity> users = Stream.of(
                new UserEntity(1L, "sanjay", "$2a$10$saIsJjjY.wZTFJeh2z/A4.4EN5.8UjXYT4/rCr3TXBW8A4yid6Eum", "")
        ).collect(Collectors.toList());

        userRepository.saveAll(users);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityWithJwtApplication.class, args);
    }

}
