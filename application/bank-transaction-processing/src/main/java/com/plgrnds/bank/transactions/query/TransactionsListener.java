package com.plgrnds.bank.transactions.query;

import com.google.common.eventbus.Subscribe;
import com.plgrnds.bank.transactions.domain.model.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionsListener {

    private final TransactionsRepository transactionsRepository;

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(TransactionCreatedEvent event) {
        TransactionProjection tx = TransactionProjection.builder()
                .version(event.getVersion())
                .timestamp(event.getTimestamp())
                .sourceAccountId(event.getSourceAccountId())
                .targetAccountId(event.getTargetAccountId())
                .value(event.getValue())
                .build();
        transactionsRepository.save(tx);
    }

}
