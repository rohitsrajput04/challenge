package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.InsufficientFundsException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class TransferController {

    private final TransferService transferService;

    private  AccountsService  accountsService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestParam String accountFromId,
                                                @RequestParam String accountToId,
                                                @RequestParam BigDecimal amount) {

        try {
            transferService.transferMoney(accountFromId, accountToId, amount);
            return ResponseEntity.ok("Transfer successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InsufficientFundsException e) {
            // Handle Insufficient Funds exception here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other generic errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
