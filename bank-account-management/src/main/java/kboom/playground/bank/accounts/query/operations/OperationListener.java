package kboom.playground.bank.accounts.query.operations;

import com.google.common.eventbus.Subscribe;
import kboom.playground.bank.accounts.domain.model.AccountDepositedEvent;
import kboom.playground.bank.accounts.domain.model.AccountWithdrawnEvent;

import static com.google.common.base.Preconditions.checkNotNull;
import static kboom.playground.bank.accounts.query.operations.OperationProjection.TransactionType.DEPOSIT;
import static kboom.playground.bank.accounts.query.operations.OperationProjection.TransactionType.WITHDRAWAL;

public class OperationListener {

    private OperationsRepository operationsRepository;

    public OperationListener(OperationsRepository operationsRepository) {
        this.operationsRepository = checkNotNull(operationsRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        OperationProjection tx = new OperationProjection(
                event.getAggregateId(), DEPOSIT, event.getAmount(), event.getTimestamp(), event.getVersion());
        operationsRepository.save(tx);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        OperationProjection tx = new OperationProjection(
                event.getAggregateId(), WITHDRAWAL, event.getAmount(), event.getTimestamp(), event.getVersion());
        operationsRepository.save(tx);
    }
}
