package com.learn.security.springsecuritywithjwt.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "USER_MASTER")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String mobile;

    public UserEntity(String username, String password, String email, String name, String mobile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.mobile = mobile;
    }
}
