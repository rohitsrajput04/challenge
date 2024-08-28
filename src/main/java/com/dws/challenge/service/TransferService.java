package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class TransferService {

    private final AccountsService accountsService;
    private final NotificationService notificationService;
    private final Lock lock = new ReentrantLock();

    @Autowired
    public TransferService(AccountsService accountsService, NotificationService notificationService) {
        this.accountsService = accountsService;
        this.notificationService = notificationService;
    }

    public void transferMoney(String accountFromId, String accountToId, BigDecimal amount) {
        lock.lock();
        try {
            Account accountFrom = accountsService.getAccount(accountFromId);
            Account accountTo = accountsService.getAccount(accountToId);

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive.");
            }

            if (accountFrom == null || accountTo == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            if (accountFrom.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in account: " + accountFromId);
            }
           // accountFrom.withdraw(amount);
            //accountTo.deposit(amount);

            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountTo.setBalance(accountTo.getBalance().add(amount));

            accountsService.createAccount(accountFrom);  // Update accounts
            accountsService.createAccount(accountTo);


            notificationService.notifyAboutTransfer(accountFrom, "Transferred " + amount + " to account " + accountToId);
            notificationService.notifyAboutTransfer(accountTo, "Received " + amount + " from account " + accountFromId);
        } finally {
            lock.unlock();
        }
    }
}
