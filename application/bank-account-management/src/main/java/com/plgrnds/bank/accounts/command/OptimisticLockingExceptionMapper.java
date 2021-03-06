package com.plgrnds.bank.accounts.command;

import com.plgrnds.bank.commons.OptimisticLockingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class OptimisticLockingExceptionMapper implements ExceptionMapper<OptimisticLockingException> {

    @Override
    public Response toResponse(OptimisticLockingException exception) {
        return Response.status(CONFLICT).build();
    }
}
