package com.plgrnds.bank.accounts.query.operations;

import java.util.List;
import java.util.UUID;

public interface OperationsRepository {

    void save(OperationProjection operationProjection);

    List<OperationProjection> listByAccount(UUID accountId);

}
