package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginAttemptEmailServiceImpl implements LoginAttemptEmailService {

    public final static int EMAIL_LOGIN_ATTEMPT = 5;
    private final UserService userService;


    @Override
    public void registerSuccessfulLogin(String email) {
        UserDto userDto = userService.findByEmail(email);
        userDto.setLoginAttempts(0);
        userService.updateUser(userDto);
    }

    @Override
    public void registerFailureLogin(String email) {
        UserDto userDto = userService.findByEmail(email);

        int loginAttempts = userDto.getLoginAttempts();
        if(loginAttempts >= EMAIL_LOGIN_ATTEMPT){
            userDto.setLoginDisabled(true);
        } else {
            userDto.setLoginAttempts(++loginAttempts);
        }

        userService.updateUser(userDto);
    }

    @Override
    public boolean isBlocked(String email) {
        UserDto userDto = userService.findByEmail(email);
        return userDto.getLoginDisabled();
    }
}
