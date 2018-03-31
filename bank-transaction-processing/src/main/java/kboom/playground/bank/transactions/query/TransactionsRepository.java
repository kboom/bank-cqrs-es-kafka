package kboom.playground.bank.transactions.query;

import java.util.List;
import java.util.UUID;

public interface TransactionsRepository {

    void save(TransactionProjection transactionProjection);

    List<TransactionProjection> listTransactionsDebiting(UUID accountId);

    List<TransactionProjection> listTransactionsCrediting(UUID accountId);

}
