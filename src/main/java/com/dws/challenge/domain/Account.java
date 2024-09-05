package com.dws.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class Account {

  @NotEmpty(message = "Account ID cannot be empty.")
  @NotNull(message = "Account ID cannot be null.")
  private final String accountId;

  @NotNull(message = "Balance cannot be null.")
  @PositiveOrZero(message = "Balance must be zero or positive")
 // @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance != null ? balance : BigDecimal.ZERO;

  }
}
