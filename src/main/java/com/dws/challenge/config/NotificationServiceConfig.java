package com.dws.challenge.config;

import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.stb.StubNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationServiceConfig {

    @Bean
    public NotificationService notificationService() {
        return new StubNotificationService();
    }
}
