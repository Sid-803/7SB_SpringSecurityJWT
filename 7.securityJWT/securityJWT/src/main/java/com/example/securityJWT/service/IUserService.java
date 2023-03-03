package com.example.securityJWT.service;

import com.example.securityJWT.entity.User;

import java.util.Optional;

public interface IUserService {

    Integer saveUser(User user);

    Optional<User> findByUsername(String username);
}
