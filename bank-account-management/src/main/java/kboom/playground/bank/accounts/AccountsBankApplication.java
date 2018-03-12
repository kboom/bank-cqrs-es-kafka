package kboom.playground.bank.accounts;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import kboom.playground.bank.accounts.command.AccountNotFoundExceptionMapper;
import kboom.playground.bank.accounts.command.AccountResource;
import kboom.playground.bank.accounts.command.AccountsResource;
import kboom.playground.bank.accounts.command.OptimisticLockingExceptionMapper;
import kboom.playground.bank.accounts.domain.service.AccountService;
import kboom.playground.bank.accounts.query.accounts.AccountsListener;
import kboom.playground.bank.accounts.query.accounts.AccountsRepository;
import kboom.playground.bank.accounts.query.accounts.ClientAccountsResource;
import kboom.playground.bank.accounts.query.accounts.InMemoryAccountsRepository;
import kboom.playground.bank.accounts.query.transactions.AccountTransactionsResource;
import kboom.playground.bank.accounts.query.transactions.InMemoryTransactionsRepository;
import kboom.playground.bank.accounts.query.transactions.TransactionsListener;
import kboom.playground.bank.accounts.query.transactions.TransactionsRepository;
import kboom.playground.bank.commons.EventStore;
import kboom.playground.bank.commons.InMemoryEventStore;
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
        TransactionsRepository transactionsRepository = new InMemoryTransactionsRepository();
        eventBus.register(new TransactionsListener(transactionsRepository));
        environment.jersey().register(new AccountTransactionsResource(transactionsRepository));

        AccountsRepository accountsRepository = new InMemoryAccountsRepository();
        eventBus.register(new AccountsListener(accountsRepository));
        environment.jersey().register(new ClientAccountsResource(accountsRepository));
    }

}
