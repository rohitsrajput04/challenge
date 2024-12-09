package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientFundsException;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

 //   @Mock
  //  private AccountsRepository accountsRepository;

    @Mock
    private NotificationService notificationService;

   // @InjectMocks
    //private TransferService transferService;

    @Mock
  private AccountsService accountsService;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

/*
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }*/

    @Test
    void testTransferMoney_Success() {
        Account accountFrom = new Account("1", BigDecimal.valueOf(100.00));
        Account accountTo = new Account("2", BigDecimal.valueOf(50.00));

        when(accountsService.getAccount("1")).thenReturn(accountFrom);
        when(accountsService.getAccount("2")).thenReturn(accountTo);

        transferService.transferMoney("1", "2", BigDecimal.valueOf(30.00));

        // Verify that the balances have been updated correctly
        assertThat(accountFrom.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(70.00));
        assertThat(accountTo.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(80.00));

        // Verify that notifications were sent with correct formatting
        verify(notificationService).notifyAboutTransfer(accountFrom, "Transferred 30.00 to account 2");
        verify(notificationService).notifyAboutTransfer(accountTo, "Received 30.00 from account 1");

    }
    @Test
    void testTransferMoney_InsufficientFunds() {
        Account accountFrom = new Account("1", BigDecimal.valueOf(10.00));  // Balance = 10.00
        Account accountTo = new Account("2", BigDecimal.valueOf(50.00));    // Balance = 50.00

        when(accountsService.getAccount("1")).thenReturn(accountFrom);
        when(accountsService.getAccount("2")).thenReturn(accountTo);

        InsufficientFundsException thrown = assertThrows(InsufficientFundsException.class, () -> {
            transferService.transferMoney("1", "2", BigDecimal.valueOf(30.00)); // Trying to transfer more than 10.00
        });

        assertEquals("Insufficient funds in account: 1", thrown.getMessage());
    }
/*
    @Test
    void testTransferMoney_InsufficientFunds() {
        Account accountFrom = new Account("1", BigDecimal.valueOf(10.00));
        Account accountTo = new Account("2", BigDecimal.valueOf(50.00));

        // Mock the service responses
        when(accountsService.getAccount("1")).thenReturn(accountFrom);
        when(accountsService.getAccount("2")).thenReturn(accountTo);

        // Assert that the RuntimeException is thrown
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transferService.transferMoney("1", "2", BigDecimal.valueOf(30.00));
        });

        assertTrue(thrown.getCause() instanceof InsufficientFundsException);
        assertEquals("Insufficient funds in account: 1", thrown.getCause().getMessage());
    }
*/

/*    @Test
    void testTransferMoney_InsufficientFunds() {
        Account accountFrom = new Account("1", BigDecimal.valueOf(10.00));
        Account accountTo = new Account("2", BigDecimal.valueOf(50.00));

        when(accountsRepository.getAccount("1")).thenReturn(accountFrom);
        when(accountsRepository.getAccount("2")).thenReturn(accountTo);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferMoney("1", "2", BigDecimal.valueOf(30.00));
        });

        assertEquals("Account not found.", thrown.getMessage());
    }*/
/*
    @Test
    void testTransferMoney_NegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            transferService.transferMoney("1", "2", BigDecimal.valueOf(-10.00));
        });

        assertEquals("Amount must be positive.", thrown.getMessage());
    }
*/

    @Test
    void testTransferMoney_NegativeAmount() {
        Account accountFrom = new Account("1", BigDecimal.valueOf(100));
        Account accountTo = new Account("2", BigDecimal.valueOf(100));
        when(accountsService.getAccount("1")).thenReturn(accountFrom);
        when(accountsService.getAccount("2")).thenReturn(accountTo);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transferService.transferMoney("1", "2", BigDecimal.valueOf(-10.00));
        });

        assertEquals("Transfer amount must be positive.", thrown.getMessage());
    }
}