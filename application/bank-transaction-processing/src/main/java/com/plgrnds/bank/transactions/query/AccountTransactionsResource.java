package com.plgrnds.bank.transactions.query;

import io.dropwizard.jersey.params.UUIDParam;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("/account/{accountId}/transactions")
@RequiredArgsConstructor
public class AccountTransactionsResource {

    private final TransactionsRepository transactionRepository;

    @GET
    public Response get(
            @PathParam("accountId") UUIDParam accountId,
            @QueryParam("type") TransactionDirection type
    ) {
        List<TransactionProjection> transactions = type.getFrom(transactionRepository, accountId.get());
        return Response.ok(transactions).build();
    }

    private enum TransactionDirection {

        DEBIT("debit"),
        CREDIT("credit");

        private String type;

        TransactionDirection(String type) {
            this.type = type;
        }

        List<TransactionProjection> getFrom(TransactionsRepository transactionsRepository, UUID accountId) {
            switch (this) {
                case DEBIT:
                    return transactionsRepository.listTransactionsDebiting(accountId);
                case CREDIT:
                    return transactionsRepository.listTransactionsCrediting(accountId);
                default:
                    throw new IllegalStateException("Could not happen");
            }
        }

    }

}
