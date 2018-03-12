package kboom.playground.bank.accounts.domain.service;

import com.google.common.eventbus.EventBus;
import kboom.playground.bank.accounts.domain.model.Account;
import kboom.playground.bank.commons.Event;
import kboom.playground.bank.commons.EventStore;
import kboom.playground.bank.commons.OptimisticLockingException;
import kboom.playground.bank.commons.Retrier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class AccountService {

    private final EventStore eventStore;
    private final EventBus eventBus;
    private final Retrier conflictRetrier;

    public AccountService(EventStore eventStore, EventBus eventBus) {
        this.eventStore = checkNotNull(eventStore);
        this.eventBus = checkNotNull(eventBus);
        int maxAttempts = 3;
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), maxAttempts);
    }

    public Optional<Account> loadAccount(UUID id) {
        List<Event> eventStream = eventStore.load(id);
        if (eventStream.isEmpty()) return Optional.empty();
        return Optional.of(new Account(id, eventStream));
    }

    public Account process(OpenAccountCommand command) {
        Account account = new Account(randomUUID(), command.getClientId());
        storeAndPublishEvents(account);
        return account;
    }

    private Account process(UUID accountId, Consumer<Account> consumer)
            throws AccountNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Account> possibleAccount = loadAccount(accountId);
            Account account = possibleAccount.orElseThrow(() -> new AccountNotFoundException(accountId));
            consumer.accept(account);
            storeAndPublishEvents(account);
            return account;
        });
    }

    private void storeAndPublishEvents(Account account) throws OptimisticLockingException {
        eventStore.store(account.getId(), account.getNewEvents(), account.getBaseVersion());
        account.getNewEvents().forEach(eventBus::post);
    }
}
