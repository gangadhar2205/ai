package com.mortgages.ai.authentication.service;

import com.mortgages.ai.authentication.config.JwtKeyProps;
import com.mortgages.ai.authentication.request.UserReq;
import com.mortgages.ai.authentication.utils.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private PrivateKey jwtPrivateKey;
    private PublicKey jwtPublicKey;


    @Autowired
    private JwtKeyProps jwtKeyProps;

    @PostConstruct
    public void initKeys() {
        this.jwtPrivateKey = KeyUtils.generatePrivateKey(jwtKeyProps.getPrivateKey());
        this.jwtPublicKey = KeyUtils.generatePublicKey(jwtKeyProps.getPublicKey());
    }

    public String createToken(UserReq authenticatedUser) {
        Date currentDate = new Date();
        Map<String, Object> claims = Map.of(
                "accountId", authenticatedUser.getUserId()
        );

        System.out.printf("Keuy Specs "+jwtKeyProps.getKeySpecs());

        Date expiry = new Date(currentDate.getTime() + jwtKeyProps.getKeySpecs() * 1000);

        System.out.printf("Curr Date "+currentDate);
        System.out.printf("Expiry Date "+expiry);

        System.out.println(jwtPrivateKey.getAlgorithm());

        return Jwts
                .builder()
                .claims()
                .add(claims)
                .and()
                .subject(authenticatedUser.getUserName())
                .issuedAt(currentDate)
                .expiration(expiry)
                .signWith(jwtPrivateKey, Jwts.SIG.RS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        System.out.println("token "+token);
        Claims claims = null;
        try {
            claims = extractAllClaims(token);
            System.out.println("claims subject" +claims.getSubject());
        } catch (Exception exception) {
            System.err.println("Failed to extract claims: " + exception.getMessage());
            exception.printStackTrace();
        }
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
       return Jwts.parser()
               .verifyWith(jwtPublicKey)
               .build()
               .parseSignedClaims(token)
               .getPayload();
   }
    public boolean validateToken(String token, UserDetails userReq) {
        System.out.println("validating token");
        final String userName = extractUserName(token);
        System.out.printf("user name "+userName);
        return userName.equals(userReq.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        System.out.println("In the user name exteaction");
        return extractClaim(token, Claims::getSubject);
    }
}
