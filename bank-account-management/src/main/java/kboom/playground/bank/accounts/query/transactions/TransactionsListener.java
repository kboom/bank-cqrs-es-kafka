package kboom.playground.bank.accounts.query.transactions;

import com.google.common.eventbus.Subscribe;
import kboom.playground.bank.accounts.domain.model.AccountDepositedEvent;
import kboom.playground.bank.accounts.domain.model.AccountWithdrawnEvent;

import static com.google.common.base.Preconditions.checkNotNull;
import static kboom.playground.bank.accounts.query.transactions.TransactionProjection.TransactionType.DEPOSIT;
import static kboom.playground.bank.accounts.query.transactions.TransactionProjection.TransactionType.WITHDRAWAL;

public class TransactionsListener {

    private TransactionsRepository transactionsRepository;

    public TransactionsListener(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = checkNotNull(transactionsRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        TransactionProjection tx = new TransactionProjection(
                event.getAggregateId(), DEPOSIT, event.getAmount(), event.getTimestamp(), event.getVersion());
        transactionsRepository.save(tx);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        TransactionProjection tx = new TransactionProjection(
                event.getAggregateId(), WITHDRAWAL, event.getAmount(), event.getTimestamp(), event.getVersion());
        transactionsRepository.save(tx);
    }
}
