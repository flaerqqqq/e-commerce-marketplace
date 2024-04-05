package com.example.ecommercemarketplace.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PublicIdGenerator {

    private final static String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generate(){
        return generateString(40);
    }

    private String generateString(int length){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHARACTERS.charAt(new Random().nextInt(CHARACTERS.length())));
        }

        return stringBuilder.toString();
    }
}
