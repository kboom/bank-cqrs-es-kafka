package kboom.playground.bank.transactions.query;

import lombok.Builder;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class TransactionProjection {

    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal value;
    private final DateTime timestamp;
    private final int version;

}
