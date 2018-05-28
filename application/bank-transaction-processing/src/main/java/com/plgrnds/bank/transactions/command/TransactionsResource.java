package com.plgrnds.bank.transactions.command;

import com.plgrnds.bank.transactions.domain.model.Transaction;
import com.plgrnds.bank.transactions.domain.service.CreateTransactionCommand;
import com.plgrnds.bank.transactions.domain.service.TransactionService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromResource;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/transactions")
public class TransactionsResource {

    private final TransactionService transactionService;

    public TransactionsResource(TransactionService transactionService) {
        this.transactionService = checkNotNull(transactionService);
    }

    @POST
    public Response post(@Valid TransactionDto transactionDto) {
        CreateTransactionCommand command = CreateTransactionCommand.builder()
                .sourceAccountId(transactionDto.getSourceAccountId())
                .targetAccountId(transactionDto.getTargetAccountId())
                .value(transactionDto.getValue())
                .build();
        Transaction account = transactionService.process(command);
        URI accountUri = fromResource(TransactionResource.class).build(account.getId());
        return Response.created(accountUri).build();
    }

}
