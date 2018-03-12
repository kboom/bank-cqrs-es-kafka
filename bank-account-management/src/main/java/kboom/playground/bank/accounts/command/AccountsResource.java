package kboom.playground.bank.accounts.command;

import kboom.playground.bank.accounts.domain.model.Account;
import kboom.playground.bank.accounts.domain.service.AccountService;
import kboom.playground.bank.accounts.domain.service.OpenAccountCommand;

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
@Path("/accounts")
public class AccountsResource {

    private final AccountService accountService;

    public AccountsResource(AccountService accountService) {
        this.accountService = checkNotNull(accountService);
    }

    @POST
    public Response post(@Valid AccountDto accountDto) {
        OpenAccountCommand command = new OpenAccountCommand(accountDto.getClientId());
        Account account = accountService.process(command);
        URI accountUri = fromResource(AccountResource.class).build(account.getId());
        return Response.created(accountUri).build();
    }
}
