package com.example.ecommercemarketplace.security;

import com.example.ecommercemarketplace.exceptions.LoginAttemptExceedingException;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.services.LoginAttemptIPService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptIPService loginAttemptIPService;
    private final LoginAttemptEmailService loginAttemptEmailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptIPService.isBlocked()) {
            throw new LoginAttemptExceedingException("Login attempts for IP_ADDRESS=%s has been exceeded".formatted(loginAttemptIPService.getClientIP()));
        } else if (loginAttemptEmailService.isBlocked(username)){
            throw new LoginAttemptExceedingException("Login attempts for EMAIL=%s has been exceeded. Return in %s".formatted(username, loginAttemptEmailService.calculateTimeToUnblock(username)));
        }

        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(userEntity);
    }
}
