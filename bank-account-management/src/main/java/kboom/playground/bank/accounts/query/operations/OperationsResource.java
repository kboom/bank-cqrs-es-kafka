package kboom.playground.bank.accounts.query.operations;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("/accounts/{id}/operations")
public class OperationsResource {

    private OperationsRepository operationsRepository;

    public OperationsResource(OperationsRepository operationsRepository) {
        this.operationsRepository = checkNotNull(operationsRepository);
    }

    @GET
    public Response get(@PathParam("id") UUID accountId) {
        List<OperationProjection> transactionProjections = operationsRepository.listByAccount(accountId);
        return Response.ok(transactionProjections).build();
    }
}
