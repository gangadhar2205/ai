package com.mortgages.ai.authentication.service;

import com.mortgages.ai.authentication.config.JwtKeyProps;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.utils.KeyUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

public class JwtService {

    private final PrivateKey jwtPrivateKey;
    private final PublicKey jwtPublicKey;

    private Integer expirySecs;

    public JwtService(JwtKeyProps keyProps) {
        this.jwtPrivateKey = KeyUtils.generatePrivateKey(keyProps.getPrivateKey());
        this.jwtPublicKey = KeyUtils.generatePublicKey(keyProps.getPublicKey());
        this.expirySecs = keyProps.getKeySpecs();
    }

    public String createToken(UserReq authenticatedUser) {
        Date date = new Date();
        Map<String, Object> claims = Map.of(
                "accountId", authenticatedUser.getUserId()
        );

        Date expiry = new Date(date.getTime() + expirySecs);

        return null;
    }
}
