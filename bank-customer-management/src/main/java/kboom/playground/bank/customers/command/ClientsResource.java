package kboom.playground.bank.customers.command;

import kboom.playground.bank.customers.domain.model.Client;
import kboom.playground.bank.customers.domain.service.ClientService;
import kboom.playground.bank.customers.domain.service.EnrollClientCommand;
import kboom.playground.bank.customers.domain.model.Email;

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
@Path("/clients")
public class ClientsResource {

    private ClientService clientService;

    public ClientsResource(ClientService clientService) {
        this.clientService = checkNotNull(clientService);
    }

    @POST
    public Response post(@Valid ClientDto newClientDto) {
        EnrollClientCommand enrollClientCommand = new EnrollClientCommand(
            newClientDto.getName(), new Email(newClientDto.getEmail()));
        Client client = clientService.process(enrollClientCommand);
        URI clientUri = fromResource(ClientResource.class).build(client.getId());
        return Response.created(clientUri).build();
    }
}
