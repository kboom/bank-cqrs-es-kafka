package kboom.playground.bank.customers.domain.model;

import kboom.playground.bank.commons.Event;
import org.joda.time.DateTime;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class CustomerUpdatedEvent extends Event {

    private final String name;
    private final String email;

    public CustomerUpdatedEvent(UUID aggregateId, DateTime timestamp, int version, String name, Email email) {
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
