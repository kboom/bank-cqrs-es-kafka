package kboom.playground.bank.customers.domain.model;

import kboom.playground.bank.commons.Aggregate;
import kboom.playground.bank.commons.Event;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;

public class Customer extends Aggregate {

    private String name;
    private Email email;

    public Customer(UUID id, String name, Email email) {
        super(id);
        validateName(name);
        validateEmail(email);
        CustomerEnrolledEvent customerEnrolledEvent = new CustomerEnrolledEvent(
                id, now(UTC), getNextVersion(), name, email);
        applyNewEvent(customerEnrolledEvent);
    }

    public Customer(UUID id, List<Event> eventStream) {
        super(id, eventStream);
    }

    public void update(String name, Email email) {
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent(
                getId(), now(UTC), getNextVersion(), name, email);
        applyNewEvent(customerUpdatedEvent);
    }

    @SuppressWarnings("unused")
    public void apply(CustomerEnrolledEvent event) {
        this.name = event.getName();
        this.email = event.getEmail();
    }

    @SuppressWarnings("unused")
    private void apply(CustomerUpdatedEvent event) {
        this.name = event.getName();
        this.email = event.getEmail();
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    private void validateName(String name) {
        checkArgument(isNotBlank(name));
    }

    private void validateEmail(Email email) {
        checkNotNull(email);
    }
}
