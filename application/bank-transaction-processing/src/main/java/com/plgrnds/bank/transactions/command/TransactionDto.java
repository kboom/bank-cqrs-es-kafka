package com.plgrnds.bank.transactions.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
class TransactionDto {

    private UUID id;
    private UUID sourceAccountId;
    private UUID targetAccountId;
    private BigDecimal value;

}
