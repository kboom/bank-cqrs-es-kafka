package kboom.playground.bank.accounts.query.operations;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class InMemoryOperationsRepository implements OperationsRepository {

    private Map<UUID, List<OperationProjection>> accountOperations = new ConcurrentHashMap<>();

    @Override
    public List<OperationProjection> listByAccount(UUID accountId) {
        return accountOperations.getOrDefault(accountId, emptyList()).stream()
                .sorted(comparing(OperationProjection::getVersion))
                .collect(toList());
    }

    @Override
    public void save(OperationProjection operationProjection) {
        accountOperations.merge(
            operationProjection.getAccountId(),
            newArrayList(operationProjection),
            (oldValue, value) -> Stream.concat(oldValue.stream(), value.stream()).collect(toList()));
    }
}
