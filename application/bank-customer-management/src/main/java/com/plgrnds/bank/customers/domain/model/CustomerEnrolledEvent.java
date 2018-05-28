package com.plgrnds.bank.customers.domain.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.plgrnds.bank.commons.Event;
import org.joda.time.DateTime;

public class CustomerEnrolledEvent extends Event {

    private final String name;
    private final String email;

    public CustomerEnrolledEvent(UUID aggregateId, DateTime timestamp, int version, String name, Email email) {
        super(aggregateId, timestamp, version);
        this.name = checkNotNull(name);
        this.email = checkNotNull(email).getValue();
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return new Email(email);
    }
}
