package kboom.playground.bank.transactions;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import kboom.playground.bank.commons.EventStore;
import kboom.playground.bank.commons.InMemoryEventStore;
import kboom.playground.bank.transactions.command.OptimisticLockingExceptionMapper;
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
        environment.jersey().register(OptimisticLockingExceptionMapper.class);
    }

    private void registerHypermediaSupport(Environment environment) {
        environment.jersey().getResourceConfig().register(DeclarativeLinkingFeature.class);
    }

    private void registerResources(Environment environment) {
        EventStore eventStore = new InMemoryEventStore();
        EventBus eventBus = new AsyncEventBus(newSingleThreadExecutor());
    }

}
