package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.LoginDataRepository;
import com.example.ecommercemarketplace.repositories.MerchantRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoginAttemptEmailServiceImpl implements LoginAttemptEmailService {

    public final static int EMAIL_LOGIN_ATTEMPT = 50;
    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final LoginDataRepository loginDataRepository;

    @Override
    public void registerSuccessfulLogin(String email) {
    }

    @Override
    public void registerFailureLogin(String email) {
        UserEntity user = userRepository.findByEmail(email).get();
        LoginData loginData = user.getLoginData();

        loginData.setLoginAttempts(loginData.getLoginAttempts() + 1);
        loginData.setLastLoginAttemptTime(LocalDateTime.now());

        if(loginData.getLoginAttempts() >= EMAIL_LOGIN_ATTEMPT){
            loginData.setLoginDisabled(true);
        }

        UserEntity savedUser = userRepository.save(user);
        System.out.println(savedUser.getLoginData());
    }

    @Override
    public boolean isBlocked(String email) {
        return false;
    }
}
