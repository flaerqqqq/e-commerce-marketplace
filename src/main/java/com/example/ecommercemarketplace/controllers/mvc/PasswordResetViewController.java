package com.example.ecommercemarketplace.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/verification")
public class PasswordResetViewController {

    @GetMapping("/password-reset")
    public String passwordResetPage() {
        return "password-reset";
    }

}
