package kboom.playground.bank.transactions.domain.service;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreateTransactionCommand {

    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal value;

}
