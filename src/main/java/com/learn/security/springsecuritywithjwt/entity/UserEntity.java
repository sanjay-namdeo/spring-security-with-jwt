package com.learn.security.springsecuritywithjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Entity
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
