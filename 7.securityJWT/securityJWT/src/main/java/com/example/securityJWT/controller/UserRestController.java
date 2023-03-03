package com.example.securityJWT.controller;

import com.example.securityJWT.entity.UserResponse;
import com.example.securityJWT.entity.User;
import com.example.securityJWT.entity.UserRequest;
import com.example.securityJWT.service.IUserService;
import com.example.securityJWT.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        Integer id = iUserService.saveUser(user);
        String message = "User with id " +id + "saved successfully";
        return ResponseEntity.ok(message);
    }

    //generate token post
    @PostMapping("/loginUser")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request){

        //Validate username/password with DB(required in case of Stateless Authentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token =jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponse(token,"Token generated successfully!"));
    }

    @PostMapping("/getData")
    public ResponseEntity<String> testAfterLogin(Principal p){
        return ResponseEntity.ok("You are accessing data after a valid Login. You are :" +p.getName());
    }

}
