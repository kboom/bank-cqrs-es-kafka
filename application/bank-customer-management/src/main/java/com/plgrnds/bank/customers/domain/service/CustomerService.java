package com.plgrnds.bank.customers.domain.service;

import com.plgrnds.bank.commons.Event;
import com.plgrnds.bank.commons.EventStore;
import com.plgrnds.bank.commons.OptimisticLockingException;
import com.plgrnds.bank.commons.Retrier;
import com.plgrnds.bank.customers.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class CustomerService {

    private final EventStore eventStore;
    private final Retrier conflictRetrier;

    public CustomerService(EventStore eventStore) {
        this.eventStore = checkNotNull(eventStore);
        int maxAttempts = 3;
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), maxAttempts);
    }

    public Customer process(EnrollCustomerCommand command) {
        Customer customer = new Customer(randomUUID(), command.getName(), command.getEmail());
        storeEvents(customer);
        return customer;
    }

    public Optional<Customer> loadCustomer(UUID id) {
        List<Event> eventStream = eventStore.load(id);
        if (eventStream.isEmpty()) return Optional.empty();
        return Optional.of(new Customer(id, eventStream));
    }

    public void process(UpdateCustomerCommand command) {
        process(command.getId(), c -> c.update(command.getName(), command.getEmail()));
    }

    private Customer process(UUID customerId, Consumer<Customer> consumer)
            throws CustomerNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Customer> possibleClient = loadCustomer(customerId);
            Customer customer = possibleClient.orElseThrow(() -> new CustomerNotFoundException(customerId));
            consumer.accept(customer);
            storeEvents(customer);
            return customer;
        });
    }

    private void storeEvents(Customer customer) {
        eventStore.store(customer.getId(), customer.getNewEvents(), customer.getBaseVersion());
    }
}
