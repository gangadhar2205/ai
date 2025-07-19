package com.mortgages.ai.authentication.service;

import com.mortgages.ai.authentication.exception.UserAlreadyExistsException;
import com.mortgages.ai.authentication.repository.BadCredentialsException;
import com.mortgages.ai.authentication.repository.UserReqRepository;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.response.AccessToken;
import com.mortgages.ai.authentication.response.Auth;
import jakarta.persistence.Access;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    private AuthenticationManager authenticationManager;

    private UserReqRepository userReqRepository;

    private SecureRandom secureRandom;

    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtService jwtService;


    public UserService(UserReqRepository userReqRepository, SecureRandom secureRandom, AuthenticationManager authenticationManager) {
        this.userReqRepository = userReqRepository;
        this.secureRandom = secureRandom;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12, secureRandom);
        this.authenticationManager = authenticationManager;
    }

    public UserReq registerUser(UserReq userReq) {

        UserReq existingUser = userReqRepository.findByUserName(userReq.getUserName());

        if(ObjectUtils.isEmpty(existingUser)) {
            throw new UserAlreadyExistsException("4009", "Email or Phone already exists");
        }

        UUID uuid = UUID.randomUUID();
        userReq.setUserId((uuid.toString()).replace("-", "").substring(0, 15));
        userReq.setPassword(bCryptPasswordEncoder.encode(userReq.getPassword()));
        userReq.setCreatedAt(new Date());
        UserReq savedUser = userReqRepository.save(userReq);

        return savedUser;
    }

    public UserReq getUserDetails(String userId) {
        return userReqRepository.findByUserId(userId);
    }

    public AccessToken validateUser(UserReq userReq) {
        AccessToken accessToken = null;
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userReq.getUserName(), userReq.getPassword()));
            if(authentication.isAuthenticated()) {
                UserReq authenticatedUser = userReqRepository.findByUserName(userReq.getUserName());
                String token = jwtService.createToken(authenticatedUser);
                accessToken = AccessToken
                        .builder()
                        .acountId(authenticatedUser.getUserId())
                        .authToken(token)
                        .build();
            }
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("4001", "Invalid credentials");
        }
        return accessToken;
    }
}
