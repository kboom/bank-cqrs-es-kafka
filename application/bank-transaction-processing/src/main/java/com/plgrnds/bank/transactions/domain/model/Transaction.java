package com.plgrnds.bank.transactions.domain.model;

import com.plgrnds.bank.commons.Aggregate;
import com.plgrnds.bank.commons.Event;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;

@Getter
public class Transaction extends Aggregate {

    private UUID sourceAccountId;
    private UUID targetAccountId;
    private BigDecimal value;

    @Builder
    public Transaction(
            UUID id,
            UUID sourceAccountId,
            UUID targetAccountId,
            BigDecimal value
    ) {
        super(id);
        TransactionCreatedEvent transactionCreatedEvent = new TransactionCreatedEvent(
                id, now(UTC), getNextVersion(), sourceAccountId, targetAccountId, value);
        applyNewEvent(transactionCreatedEvent);
    }

    public Transaction(UUID id, List<Event> eventStream) {
        super(id, eventStream);
    }

    @SuppressWarnings("unused")
    private void apply(TransactionCreatedEvent event) {
        sourceAccountId = event.getSourceAccountId();
        targetAccountId = event.getTargetAccountId();
        value = event.getValue();
    }

}
