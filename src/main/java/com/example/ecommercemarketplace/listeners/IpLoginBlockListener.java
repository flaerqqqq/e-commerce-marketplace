package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.events.IpLoginBlockEvent;
import com.example.ecommercemarketplace.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IpLoginBlockListener implements ApplicationListener<IpLoginBlockEvent> {

    @Override
    public void onApplicationEvent(IpLoginBlockEvent event) {
        log.info("Someone with IP_ADDRESS={} is suspended to login.", event.getIpAddress());
    }
}
