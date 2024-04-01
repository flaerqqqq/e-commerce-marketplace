package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.UserRegistrationRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @PostMapping("/test")
    public String getDemo(@RequestBody UserRegistrationRequest registrationRequest){

        System.out.println(registrationRequest);
        return "test";
    }
}
