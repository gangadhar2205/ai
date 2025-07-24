package com.mortgages.ai.authentication.service;

import java.security.*;
import java.util.Base64;

public class Demo {

    public static String getBase64Key(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static void arrays(String[] args) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();

            String privateKeyString = getBase64Key(keyPair.getPrivate());
            String publicKeyString = getBase64Key(keyPair.getPublic());

            System.out.println("-----BEGIN PRIVATE KEY-----");
            System.out.println(privateKeyString);
            System.out.println("-----END PRIVATE KEY-----");

            System.out.println("-----BEGIN PUBLIC KEY-----");
            System.out.println(publicKeyString);
            System.out.println("-----END PUBLIC KEY-----");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
