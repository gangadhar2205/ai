package com.mortgages.ai.authentication.service;

import com.mortgages.ai.authentication.config.UserPrincipal;
import com.mortgages.ai.authentication.repository.UserReqRepository;
import com.mortgages.ai.authentication.request.UserReq;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private UserReqRepository userReqRepository;

    public AuthUserDetailsService(UserReqRepository userReqRepository) {
        this.userReqRepository = userReqRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserReq userReq = userReqRepository.findByUserName(username);
        return new UserPrincipal(userReq);
    }
}
