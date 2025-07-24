package com.mortgages.ai.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.response.AccessToken;
import com.mortgages.ai.authentication.response.AgenticAipResponse;
import com.mortgages.ai.authentication.response.Auth;
import com.mortgages.ai.authentication.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "user/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgenticAipResponse> registerUser(@RequestBody UserReq userReq) throws JsonProcessingException {
        AgenticAipResponse registeredUser = userService.registerUser(userReq);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @GetMapping(value = "user/profile")
    public ResponseEntity<UserReq> getUser(@RequestParam String userId) {
        System.out.printf("userId "+userId);
        UserReq registeredUser = userService.getUserDetails(userId);
        registeredUser.setPassword(null);
        return ResponseEntity.status(200).body(registeredUser);
    }

    @PostMapping(value = "user/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Auth> login(@RequestBody UserReq userReq) {
        AccessToken accessToken = userService.validateUser(userReq);
        Auth authresp =  Auth
                .builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.status(200).body(authresp);
    }
}
