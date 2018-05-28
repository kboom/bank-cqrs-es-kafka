package com.plgrnds.bank.accounts.domain.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.UUID;

import com.plgrnds.bank.commons.Event;
import org.joda.time.DateTime;

public class AccountOpenedEvent extends Event {

    private final String clientId;
    private final BigDecimal balance;

    public AccountOpenedEvent(UUID aggregateId, DateTime timestamp, int version, UUID clientId, BigDecimal balance) {
        super(aggregateId, timestamp, version);
        this.clientId = checkNotNull(clientId).toString();
        this.balance = checkNotNull(balance);
    }

    public UUID getClientId() {
        return UUID.fromString(clientId);
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
