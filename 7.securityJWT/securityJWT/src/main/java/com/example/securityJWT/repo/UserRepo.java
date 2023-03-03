package com.example.securityJWT.repo;

import com.example.securityJWT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {

    @Query("Select userName from User where wserName = ?")
    Optional<User> findUserByUserName(String userName);
}
