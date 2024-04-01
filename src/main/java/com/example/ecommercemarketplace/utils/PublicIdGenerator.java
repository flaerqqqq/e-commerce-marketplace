package com.example.ecommercemarketplace.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PublicIdGenerator {

    private String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generate(){
        return generateString(40);
    }

    private String generateString(int length){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(new Random().nextInt(characters.length())));
        }

        return stringBuilder.toString();
    }
}
