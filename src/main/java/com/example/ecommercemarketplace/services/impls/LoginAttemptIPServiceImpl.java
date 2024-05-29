package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.events.IpLoginBlockEvent;
import com.example.ecommercemarketplace.services.LoginAttemptIPService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptIPServiceImpl implements LoginAttemptIPService {

    public static final int IP_MAX_ATTEMPT = 10;
    private final LoadingCache<String, Integer> cachedAttempts;
    private ApplicationEventPublisher applicationEventPublisher;
    private HttpServletRequest httpServletRequest;

    public LoginAttemptIPServiceImpl(ApplicationEventPublisher applicationEventPublisher,
                                     HttpServletRequest httpServletRequest) {
        cachedAttempts = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) throws Exception {
                return 0;
            }
        });
        this.applicationEventPublisher = applicationEventPublisher;
        this.httpServletRequest = httpServletRequest;
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
        if(attempts == 10){
            applicationEventPublisher.publishEvent(new IpLoginBlockEvent(this, ipAddressKey));
        }
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
            return cachedAttempts.get(getClientIP()) >= IP_MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
    }

    public String getClientIP() {
        final String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");

        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }

        return httpServletRequest.getRemoteAddr();
    }
}
