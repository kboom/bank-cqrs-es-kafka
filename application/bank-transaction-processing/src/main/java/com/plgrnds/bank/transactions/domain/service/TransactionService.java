package com.plgrnds.bank.transactions.domain.service;

import com.google.common.eventbus.EventBus;
import com.plgrnds.bank.commons.Event;
import com.plgrnds.bank.commons.EventStore;
import com.plgrnds.bank.commons.OptimisticLockingException;
import com.plgrnds.bank.commons.Retrier;
import com.plgrnds.bank.transactions.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class TransactionService {

    private static final int MAX_ATTEMPTS = 3;

    private final EventStore eventStore;
    private final EventBus eventBus;
    private final Retrier conflictRetrier;

    public TransactionService(EventStore eventStore, EventBus eventBus) {
        this.eventStore = checkNotNull(eventStore);
        this.eventBus = checkNotNull(eventBus);
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), MAX_ATTEMPTS);
    }

    public Optional<Transaction> loadTransaction(UUID id) {
        List<Event> eventStream = eventStore.load(id);
        if (eventStream.isEmpty()) return Optional.empty();
        return Optional.of(new Transaction(id, eventStream));
    }

    public Transaction process(CreateTransactionCommand command) {
        Transaction transaction = Transaction.builder()
                .id(randomUUID())
                .sourceAccountId(command.getSourceAccountId())
                .targetAccountId(command.getTargetAccountId())
                .value(command.getValue())
                .build();
        storeAndPublishEvents(transaction);
        return transaction;
    }

    @SuppressWarnings("unused")
    private Transaction process(UUID accountId, Consumer<Transaction> consumer)
            throws TransactionNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Transaction> possibleAccount = loadTransaction(accountId);
            Transaction transaction = possibleAccount.orElseThrow(() -> new TransactionNotFoundException(accountId));
            consumer.accept(transaction);
            storeAndPublishEvents(transaction);
            return transaction;
        });
    }

    private void storeAndPublishEvents(Transaction transaction) throws OptimisticLockingException {
        eventStore.store(transaction.getId(), transaction.getNewEvents(), transaction.getBaseVersion());
        transaction.getNewEvents().forEach(eventBus::post);
    }

}
