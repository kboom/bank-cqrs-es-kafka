package kboom.playground.bank.customers.command;

import io.dropwizard.jersey.params.UUIDParam;
import kboom.playground.bank.customers.domain.model.Client;
import kboom.playground.bank.customers.domain.service.ClientService;
import kboom.playground.bank.customers.domain.service.UpdateClientCommand;
import kboom.playground.bank.customers.domain.model.Email;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/clients/{id}")
public class ClientResource {

    private ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = checkNotNull(clientService);
    }

    @GET
    public Response get(@PathParam("id") UUIDParam clientId) {
        Optional<Client> possibleClient = clientService.loadClient(clientId.get());
        if (!possibleClient.isPresent()) return Response.status(NOT_FOUND).build();
        ClientDto clientDto = toDto(possibleClient.get());
        return Response.ok(clientDto).build();
    }

    @PUT
    public Response put(@PathParam("id") UUIDParam clientId, @Valid @NotNull ClientDto clientDto) {
        UpdateClientCommand command = new UpdateClientCommand(
                clientId.get(), clientDto.getName(), new Email(clientDto.getEmail()));
        clientService.process(command);
        return Response.noContent().build();
    }

    private ClientDto toDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail().getValue());
        return dto;
    }
}
