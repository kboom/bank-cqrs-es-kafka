package com.plgrnds.bank.accounts.query.accounts;

import org.junit.Test;

import java.util.UUID;

import static java.math.BigDecimal.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InMemoryAccountsRepositoryTest {

    private AccountsRepository accountsRepository =
            new InMemoryAccountsRepository();

    @Test
    public void ignoreEventOutOfOrder() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        accountsRepository.save(new AccountProjection(accountId, clientId, ZERO, 1));
        accountsRepository.updateBalance(accountId, TEN, 3);
        accountsRepository.updateBalance(accountId, ONE, 2);
        assertThat(accountsRepository.getAccounts(clientId).get(0).getBalance(), equalTo(TEN));
    }
}