package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.events.EmailLoginBlockEvent;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.LoginDataRepository;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LoginAttemptEmailServiceImpl implements LoginAttemptEmailService {

    public final static int EMAIL_LOGIN_ATTEMPT = 5;
    private final UserRepository userRepository;
    private final LoginDataRepository loginDataRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void registerSuccessfulLogin(String email) {
        UserEntity user = findUserByEmail(email);

        LoginData loginData = loginDataRepository.findByUser(user);
        loginData.setLoginAttempts(0);
        loginData.setLastLoginTime(LocalDateTime.now());
        loginData.setLastLoginAttemptTime(null);

        loginDataRepository.save(loginData);
    }

    @Override
    public void registerFailureLogin(String email) {
        UserEntity user = findUserByEmail(email);

        LoginData loginData = loginDataRepository.findByUser(user);
        loginData.setLoginAttempts(loginData.getLoginAttempts() + 1);
        loginData.setLastLoginAttemptTime(LocalDateTime.now());

        blockUserLogin(loginData);

        loginDataRepository.save(loginData);
    }

    private void blockUserLogin(LoginData loginData) {
        if (loginData.getLoginAttempts() >= EMAIL_LOGIN_ATTEMPT) {
            loginData.setLoginDisabled(true);
        }
        applicationEventPublisher.publishEvent(new EmailLoginBlockEvent(this, loginData.getUser().getEmail()));
    }

    @Override
    public boolean isBlocked(String email) {
        UserEntity user = findUserByEmail(email);
        return user.getLoginData().isLoginDisabled();
    }

    @Override
    public void unblockUsersLogin() {
        List<LoginData> blockedUsersLoginData = loginDataRepository.findAllByLoginDisabled(true);

        for (LoginData loginData : blockedUsersLoginData) {
            LocalDateTime lastLoginAttemptTime = loginData.getLastLoginAttemptTime();
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(lastLoginAttemptTime.plusHours(1))) {
                loginData.setLoginDisabled(false);
                loginData.setLoginAttempts(0);
                loginData.setLastLoginAttemptTime(null);
                loginDataRepository.save(loginData);
            }
        }
    }

    @Override
    public void unblockUserLogin(String email) {
        UserEntity user = findUserByEmail(email);

        LoginData loginData = loginDataRepository.findByUser(user);
        loginData.setLoginDisabled(false);
        loginData.setLoginAttempts(0);
        loginData.setLastLoginAttemptTime(null);

        loginDataRepository.save(loginData);
    }

    @Override
    public String calculateTimeToUnblock(String email) {
        UserEntity user = findUserByEmail(email);
        LoginData loginData = loginDataRepository.findByUser(user);

        Duration duration = Duration.between(LocalDateTime.now(), loginData.getLastLoginAttemptTime().plusHours(2));

        return duration.toMinutesPart() + " minutes";
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User with email=%s is not found".formatted(email)));
    }
}
