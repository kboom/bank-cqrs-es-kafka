package com.plgrnds.bank.accounts.query.operations;

import com.google.common.eventbus.Subscribe;
import com.plgrnds.bank.accounts.domain.model.AccountDepositedEvent;
import com.plgrnds.bank.accounts.domain.model.AccountWithdrawnEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public class OperationListener {

    private OperationsRepository operationsRepository;

    public OperationListener(OperationsRepository operationsRepository) {
        this.operationsRepository = checkNotNull(operationsRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        OperationProjection tx = new OperationProjection(
                event.getAggregateId(), OperationProjection.TransactionType.DEPOSIT, event.getAmount(), event.getTimestamp(), event.getVersion());
        operationsRepository.save(tx);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        OperationProjection tx = new OperationProjection(
                event.getAggregateId(), OperationProjection.TransactionType.WITHDRAWAL, event.getAmount(), event.getTimestamp(), event.getVersion());
        operationsRepository.save(tx);
    }
}
