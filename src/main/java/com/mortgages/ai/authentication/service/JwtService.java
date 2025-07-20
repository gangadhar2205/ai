package com.mortgages.ai.authentication.service;

import com.mortgages.ai.authentication.config.JwtKeyProps;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.utils.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

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
        Date currentDate = new Date();
        Map<String, Object> claims = Map.of(
                "accountId", authenticatedUser.getUserId()
        );

        Date expiry = new Date(currentDate.getTime() + expirySecs);

        return Jwts
                .builder()
                .addClaims(claims)
                .setSubject(authenticatedUser.getUserName())
                .setIssuedAt(currentDate)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.RS256, jwtPrivateKey)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = null;
        try {
            claims = extractAllClaims(token);
        } catch (Exception exception) {

        }
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
       return Jwts.parser()
               .setSigningKey(jwtPublicKey)
               .parseClaimsJws(token)
               .getBody();
   }
    public boolean validateToken(String token, UserDetails userReq) {
        final String userName = extractUserName(token);
        return userName.equals(userReq.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
