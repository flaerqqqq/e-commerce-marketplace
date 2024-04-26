package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.LoginDataRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class LoginAttemptEmailServiceImpl implements LoginAttemptEmailService {

    public final static int EMAIL_LOGIN_ATTEMPT = 5;
    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final LoginDataRepository loginDataRepository;

    @Override
    public void registerSuccessfulLogin(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
        LoginData loginData = loginDataRepository.findByUser(user);

        loginData.setLoginAttempts(0);
        loginData.setLastLoginTime(LocalDateTime.now());
        loginData.setLastLoginAttemptTime(null);

        loginDataRepository.save(loginData);
    }

    @Override
    public void registerFailureLogin(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
        LoginData loginData = loginDataRepository.findByUser(user);

        loginData.setLoginAttempts(loginData.getLoginAttempts() + 1);
        loginData.setLastLoginAttemptTime(LocalDateTime.now());

        if (loginData.getLoginAttempts() >= EMAIL_LOGIN_ATTEMPT) {
            loginData.setLoginDisabled(true);
        }

        loginDataRepository.save(loginData);
    }

    @Override
    public boolean isBlocked(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
        return user.getLoginData().isLoginDisabled();
    }

    @Override
    public void unblockUserLogin() {
        List<LoginData> blockedUsersLoginData = loginDataRepository.findAllByLoginDisabled(true);

        for (LoginData loginData : blockedUsersLoginData) {
            LocalDateTime lastLoginAttemptTime = loginData.getLastLoginAttemptTime();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(lastLoginAttemptTime.plus(1, ChronoUnit.HOURS))) {
                loginData.setLoginDisabled(false);
                loginData.setLoginAttempts(0);
                loginData.setLastLoginAttemptTime(null);
                loginDataRepository.save(loginData);
            }
        }
    }

    @Override
    public String calculateTimeToUnblock(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
        LoginData loginData = loginDataRepository.findByUser(user);

        Duration duration = Duration.between(LocalDateTime.now(), loginData.getLastLoginAttemptTime().plusHours(2));

        return duration.toMinutesPart() + " minutes";
    }
}
