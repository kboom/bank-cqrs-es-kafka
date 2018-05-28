package com.plgrnds.bank.accounts.domain.service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.plgrnds.bank.commons.EventStore;
import com.plgrnds.bank.commons.InMemoryEventStore;
import org.junit.Before;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.spy;

public class AccountServiceTest {

    private EventStore eventStore;
    private EventBusCounter eventBusCounter;
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        eventStore = spy(new InMemoryEventStore());
        EventBus eventBus = new EventBus();
        eventBusCounter = new EventBusCounter();
        eventBus.register(eventBusCounter);
        accountService = new AccountService(eventStore, eventBus);
    }

    private static class EventBusCounter {
        Map<Class<?>, Integer> eventsCount = new ConcurrentHashMap<>();

        @Subscribe
        @SuppressWarnings("unused")
        public void handle(Object event) {
            eventsCount.merge(event.getClass(), 1, (oldValue, value) -> oldValue + value);
        }
    }

}
