package com.example.ecommercemarketplace.security;

import com.example.ecommercemarketplace.exceptions.LoginAttemptExceedingException;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.UserRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptIPService.isBlocked()) {
            throw new LoginAttemptExceedingException("Login attempts for IP_ADDRESS=%s has been exceeded".formatted(loginAttemptIPService.getClientIP()));
        }

        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new CustomUserDetails(userEntity);
    }
}
