package com.plgrnds.bank.accounts.query.accounts;

import com.google.common.eventbus.Subscribe;
import com.plgrnds.bank.accounts.domain.model.AccountDepositedEvent;
import com.plgrnds.bank.accounts.domain.model.AccountOpenedEvent;
import com.plgrnds.bank.accounts.domain.model.AccountWithdrawnEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountsListener {

    private final AccountsRepository accountsRepository;

    public AccountsListener(AccountsRepository accountsRepository) {
        this.accountsRepository = checkNotNull(accountsRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountOpenedEvent event) {
        AccountProjection accountProjection = new AccountProjection(
            event.getAggregateId(), event.getClientId(), event.getBalance(), event.getVersion());
        accountsRepository.save(accountProjection);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        accountsRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        accountsRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }

}
