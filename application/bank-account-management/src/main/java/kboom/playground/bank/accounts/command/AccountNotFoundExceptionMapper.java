package kboom.playground.bank.accounts.command;

import kboom.playground.bank.accounts.domain.service.AccountNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {

    @Override
    public Response toResponse(AccountNotFoundException exception) {
        return Response.status(NOT_FOUND).build();
    }
}
