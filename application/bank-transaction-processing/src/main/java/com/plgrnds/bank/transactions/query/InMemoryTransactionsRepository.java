package com.plgrnds.bank.transactions.query;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class InMemoryTransactionsRepository implements TransactionsRepository {

    private Map<UUID, List<TransactionProjection>> debitingTransactions = new ConcurrentHashMap<>();
    private Map<UUID, List<TransactionProjection>> creditingTransactions = new ConcurrentHashMap<>();

    @Override
    public void save(TransactionProjection transactionProjection) {
        debitingTransactions.merge(
                transactionProjection.getSourceAccountId(),
                Lists.newArrayList(transactionProjection),
                (oldValue, value) -> Stream.concat(oldValue.stream(), value.stream()).collect(toList()));

        creditingTransactions.merge(
                transactionProjection.getTargetAccountId(),
                Lists.newArrayList(transactionProjection),
                (oldValue, value) -> Stream.concat(oldValue.stream(), value.stream()).collect(toList()));
    }

    @Override
    public List<TransactionProjection> listTransactionsDebiting(UUID accountId) {
        return debitingTransactions.getOrDefault(accountId, emptyList()).stream()
                .sorted(comparing(TransactionProjection::getVersion))
                .collect(toList());
    }

    @Override
    public List<TransactionProjection> listTransactionsCrediting(UUID accountId) {
        return creditingTransactions.getOrDefault(accountId, emptyList()).stream()
                .sorted(comparing(TransactionProjection::getVersion))
                .collect(toList());
    }

}
