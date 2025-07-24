package com.mortgages.ai.authentication.utils;


import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    public static PrivateKey generatePrivateKey(String key) {
        try {
            byte[] encoded = Base64.getDecoder().decode(key);
            System.out.println("Key "+key);
            PKCS8EncodedKeySpec keySpecification = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpecification);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    public static PublicKey generatePublicKey(String key) {
        try {
            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException invalidKeySpecException) {
            throw new RuntimeException(invalidKeySpecException);
        }
    }
}
