package kboom.playground.bank.transactions.command;

import io.dropwizard.jersey.params.UUIDParam;
import kboom.playground.bank.transactions.domain.model.Transaction;
import kboom.playground.bank.transactions.domain.service.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/transactions/{id}")
public class TransactionResource {

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @GET
    public Response get(@PathParam("id") UUIDParam accountId) {
        Optional<Transaction> possibleTransaction = transactionService.loadTransaction(accountId.get());
        if (!possibleTransaction.isPresent()) return Response.status(NOT_FOUND).build();
        TransactionDto accountDto = toDto(possibleTransaction.get());
        return Response.ok(accountDto).build();
    }

    private TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setSourceAccountId(transaction.getSourceAccountId());
        dto.setTargetAccountId(transaction.getTargetAccountId());
        dto.setValue(transaction.getValue());
        return dto;
    }

}
