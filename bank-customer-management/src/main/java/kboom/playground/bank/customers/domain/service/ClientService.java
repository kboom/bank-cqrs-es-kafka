package kboom.playground.bank.customers.domain.service;

import kboom.playground.bank.commons.Event;
import kboom.playground.bank.commons.EventStore;
import kboom.playground.bank.commons.OptimisticLockingException;
import kboom.playground.bank.commons.Retrier;
import kboom.playground.bank.customers.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class ClientService {

    private final EventStore eventStore;
    private final Retrier conflictRetrier;

    public ClientService(EventStore eventStore) {
        this.eventStore = checkNotNull(eventStore);
        int maxAttempts = 3;
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), maxAttempts);
    }

    public Client process(EnrollClientCommand command) {
        Client client = new Client(randomUUID(), command.getName(), command.getEmail());
        storeEvents(client);
        return client;
    }

    public Optional<Client> loadClient(UUID id) {
        List<Event> eventStream = eventStore.load(id);
        if (eventStream.isEmpty()) return Optional.empty();
        return Optional.of(new Client(id, eventStream));
    }

    public void process(UpdateClientCommand command) {
        process(command.getId(), c -> c.update(command.getName(), command.getEmail()));
    }

    private Client process(UUID clientId, Consumer<Client> consumer)
            throws ClientNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Client> possibleClient = loadClient(clientId);
            Client client = possibleClient.orElseThrow(() -> new ClientNotFoundException(clientId));
            consumer.accept(client);
            storeEvents(client);
            return client;
        });
    }

    private void storeEvents(Client client) {
        eventStore.store(client.getId(), client.getNewEvents(), client.getBaseVersion());
    }
}
