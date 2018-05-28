package com.plgrnds.bank.transactions;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import com.plgrnds.bank.commons.EventStore;
import com.plgrnds.bank.commons.InMemoryEventStore;
import com.plgrnds.bank.transactions.command.OptimisticLockingExceptionMapper;
import com.plgrnds.bank.transactions.command.TransactionResource;
import com.plgrnds.bank.transactions.command.TransactionsResource;
import com.plgrnds.bank.transactions.domain.service.TransactionService;
import com.plgrnds.bank.transactions.query.AccountTransactionsResource;
import com.plgrnds.bank.transactions.query.InMemoryTransactionsRepository;
import com.plgrnds.bank.transactions.query.TransactionsListener;
import com.plgrnds.bank.transactions.query.TransactionsRepository;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

public class TransactionsBankApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new TransactionsBankApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        registerFilters(environment);
        registerExceptionMappers(environment);
        registerHypermediaSupport(environment);
        registerResources(environment);
    }

    private void registerFilters(Environment environment) {
        environment.jersey().register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
    }

    private void registerExceptionMappers(Environment environment) {
        environment.jersey().register(OptimisticLockingExceptionMapper.class);
    }

    private void registerHypermediaSupport(Environment environment) {
        environment.jersey().getResourceConfig().register(DeclarativeLinkingFeature.class);
    }

    private void registerResources(Environment environment) {
        EventStore eventStore = new InMemoryEventStore();
        EventBus eventBus = new AsyncEventBus(newSingleThreadExecutor());

        // write model
        TransactionService transactionService = new TransactionService(eventStore, eventBus);
        environment.jersey().register(new TransactionsResource(transactionService));
        environment.jersey().register(new TransactionResource(transactionService));

        // read model
        TransactionsRepository transactionsRepository = new InMemoryTransactionsRepository();
        eventBus.register(new TransactionsListener(transactionsRepository));
        environment.jersey().register(new AccountTransactionsResource(transactionsRepository));
    }

}
