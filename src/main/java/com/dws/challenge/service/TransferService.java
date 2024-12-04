package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
        DecimalFormat df = new DecimalFormat("0.00");

        try {
            log.info("Start transfer: From Account ID: {}, To Account ID: {}, Amount: {}", accountFromId, accountToId, amount);

            Account accountFrom = accountsService.getAccount(accountFromId);
            log.info("Account 'from' retrieved: {}", accountFrom);

            Account accountTo = accountsService.getAccount(accountToId);
            log.info("Account 'to' retrieved: {}", accountTo);

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive.");
            }

            if (accountFrom == null || accountTo == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            if (accountFrom.getBalance() == null || accountFrom.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in account: " + accountFromId);
            }

            log.info("Processing transfer: {} from {} to {}", df.format(amount), accountFromId, accountToId);

            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountTo.setBalance(accountTo.getBalance().add(amount));

            log.info("Updated balances: Account 'from': {}, Account 'to': {}", accountFrom.getBalance(), accountTo.getBalance());

            // Instead of createAccount(), use updateAccount()
            accountsService.updateAccount(accountFrom);  // Update the 'from' account
            accountsService.updateAccount(accountTo);    // Update the 'to' account

            notificationService.notifyAboutTransfer(accountFrom, "Transferred " + df.format(amount) + " to account " + accountToId);
            notificationService.notifyAboutTransfer(accountTo, "Received " + df.format(amount) + " from account " + accountFromId);
        } catch (Exception e) {
            log.error("Error occurred during transfer: {}", e.getMessage(), e);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e); // Re-throw or handle as appropriate
        } finally {
            lock.unlock();
        }
    }

}
