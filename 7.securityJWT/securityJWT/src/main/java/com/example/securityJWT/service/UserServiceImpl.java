package com.example.securityJWT.service;

import com.example.securityJWT.entity.User;
import com.example.securityJWT.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public Integer saveUser(User user) {
        return userRepo.save(user).getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findUserByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepo.findUserByUserName(username);
        org.springframework.security.core.userdetails.User springUser = null;

        if(opt.isEmpty())
        {
            throw new UsernameNotFoundException("User doesnt exist");
        }
        else
        {
            User user = opt.get(); //retirieving user from db
            Set<String> roles = user.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            for(String role:roles)
            {
                ga.add(new SimpleGrantedAuthority(role));
            }

            springUser = new org.springframework.security.core.userdetails.User(
                    username,
                    user.getPassword(),
                    ga
            );
            }
        return springUser;

    }
}
