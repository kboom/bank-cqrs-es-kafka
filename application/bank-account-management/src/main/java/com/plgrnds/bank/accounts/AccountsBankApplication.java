package com.plgrnds.bank.accounts;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.plgrnds.bank.accounts.command.AccountNotFoundExceptionMapper;
import com.plgrnds.bank.accounts.command.AccountResource;
import com.plgrnds.bank.accounts.command.AccountsResource;
import com.plgrnds.bank.accounts.command.OptimisticLockingExceptionMapper;
import com.plgrnds.bank.accounts.domain.service.AccountService;
import com.plgrnds.bank.accounts.query.accounts.AccountsListener;
import com.plgrnds.bank.accounts.query.accounts.AccountsRepository;
import com.plgrnds.bank.accounts.query.accounts.ClientAccountsResource;
import com.plgrnds.bank.accounts.query.accounts.InMemoryAccountsRepository;
import com.plgrnds.bank.accounts.query.operations.InMemoryOperationsRepository;
import com.plgrnds.bank.accounts.query.operations.OperationListener;
import com.plgrnds.bank.accounts.query.operations.OperationsRepository;
import com.plgrnds.bank.accounts.query.operations.OperationsResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import com.plgrnds.bank.commons.EventStore;
import com.plgrnds.bank.commons.InMemoryEventStore;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.logging.LoggingFeature;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

public class AccountsBankApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new AccountsBankApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        registerFilters(environment);
        registerExceptionMappers(environment);
        registerHypermediaSupport(environment);
        registerResources(environment);
    }

    private void registerFilters(Environment environment) {
        environment.jersey().register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
    }

    private void registerExceptionMappers(Environment environment) {
        environment.jersey().register(AccountNotFoundExceptionMapper.class);
        environment.jersey().register(OptimisticLockingExceptionMapper.class);
    }

    private void registerHypermediaSupport(Environment environment) {
        environment.jersey().getResourceConfig().register(DeclarativeLinkingFeature.class);
    }

    private void registerResources(Environment environment) {
        EventStore eventStore = new InMemoryEventStore();
        EventBus eventBus = new AsyncEventBus(newSingleThreadExecutor());

        // write model
        AccountService accountService = new AccountService(eventStore, eventBus);
        environment.jersey().register(new AccountsResource(accountService));
        environment.jersey().register(new AccountResource(accountService));

        // read model
        OperationsRepository transactionsRepository = new InMemoryOperationsRepository();
        eventBus.register(new OperationListener(transactionsRepository));
        environment.jersey().register(new OperationsResource(transactionsRepository));

        AccountsRepository accountsRepository = new InMemoryAccountsRepository();
        eventBus.register(new AccountsListener(accountsRepository));
        environment.jersey().register(new ClientAccountsResource(accountsRepository));
    }

}
