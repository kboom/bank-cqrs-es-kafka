package kboom.playground.bank.accounts.query.accounts;

import io.dropwizard.jersey.params.UUIDParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("clients/{id}/accounts")
public class ClientAccountsResource {

    private final AccountsRepository accountsRepository;

    public ClientAccountsResource(AccountsRepository accountsRepository) {
        this.accountsRepository = checkNotNull(accountsRepository);
    }

    @GET
    public Response get(@PathParam("id") UUIDParam clientId) {
        List<AccountProjection> accounts = accountsRepository.getAccounts(clientId.get());
        return Response.ok(accounts).build();
    }
}
