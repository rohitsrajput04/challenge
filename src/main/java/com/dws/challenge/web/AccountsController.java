package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);


    if (account.getAccountId() == null || account.getAccountId().trim().isEmpty() ) {

      return new ResponseEntity<>("Account ID cannot be empty.", HttpStatus.BAD_REQUEST);
    }

    if (account.getBalance() == null) {

      return new ResponseEntity<>("Balance cannot be null.", HttpStatus.BAD_REQUEST);
    }
    if (account.getBalance().toString().trim().isEmpty()) {
      return new ResponseEntity<>("Balance cannot be empty.", HttpStatus.BAD_REQUEST);
    }

    if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {

      return new ResponseEntity<>("Balance cannot be negative.", HttpStatus.BAD_REQUEST);
    }
    try {
      this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }


    @GetMapping(path = "/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
      log.info("Retrieving account for id {}", accountId);

      Account account = accountsService.getAccount(accountId);
      if (account == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(account, HttpStatus.OK);
    }
  }

/*
  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }*/

