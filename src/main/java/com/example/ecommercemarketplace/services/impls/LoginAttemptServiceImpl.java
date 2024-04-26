package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.services.LoginAttemptService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    public static final int MAX_ATTEMPT = 5;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private LoadingCache<String, Integer> cachedAttempts;

    public LoginAttemptServiceImpl(){
        cachedAttempts = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) throws Exception {
                return 0;
            }
        });
    }

    @Override
    public void registerFailedLogin(String ipAddressKey) {
        int attempts;

        try {
            attempts = cachedAttempts.get(ipAddressKey);
        } catch (Exception e) {
            attempts = 0;
        }

        attempts++;
        cachedAttempts.put(ipAddressKey, attempts);
    }

    @Override
    public void registerSuccessLogin(String ipAddressKey) {
        try {
            cachedAttempts.get(ipAddressKey);
        } catch (Exception e) {
            cachedAttempts.put(ipAddressKey, 0);
        }
    }

    @Override
    public boolean isBlocked() {
        try {
           return cachedAttempts.get(getIP()) >= MAX_ATTEMPT;
        } catch (Exception e){
            return false;
        }
    }

    private String getIP(){
        final String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");

        if (xfHeader != null){
            return xfHeader.split(",")[0];
        }

        return httpServletRequest.getRemoteAddr();
    }
}
