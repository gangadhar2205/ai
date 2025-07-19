package com.mortgages.ai.authentication.controller;

import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.response.AccessToken;
import com.mortgages.ai.authentication.response.Auth;
import com.mortgages.ai.authentication.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReq> registerUser(@RequestBody UserReq userReq) {
        UserReq registeredUser = userService.registerUser(userReq);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @PostMapping(value = "/user/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReq> getUser(@RequestParam String userId) {
        UserReq registeredUser = userService.getUserDetails(userId);
        registeredUser.setPassword(null);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @PostMapping(value = "/user/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Auth> login(@RequestBody UserReq userReq) {
        AccessToken accessToken = userService.validateUser(userReq);
        return null;
    }
}
