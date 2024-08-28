package com.dws.challenge.service.stb;

import com.dws.challenge.domain.Account;
import com.dws.challenge.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class StubNotificationService implements NotificationService {

    @Override
    public void notifyAboutTransfer(Account account, String transferDescription) {
        System.out.println("Notification sent to account " + account.getAccountId() + ": " + transferDescription);
    }
}
