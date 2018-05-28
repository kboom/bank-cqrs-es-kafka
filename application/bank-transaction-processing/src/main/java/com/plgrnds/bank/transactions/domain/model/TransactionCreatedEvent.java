package com.plgrnds.bank.transactions.domain.model;

import com.plgrnds.bank.commons.Event;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TransactionCreatedEvent extends Event {

    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal value;

    TransactionCreatedEvent(
            UUID aggregateId,
            DateTime timestamp,
            int version,
            UUID sourceAccountId,
            UUID targetAccountId,
            BigDecimal value
    ) {
        super(aggregateId, timestamp, version);
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.value = value;
    }

}
