package kboom.playground.bank.customers.command;

import io.dropwizard.jersey.params.UUIDParam;
import kboom.playground.bank.customers.domain.model.Customer;
import kboom.playground.bank.customers.domain.service.CustomerService;
import kboom.playground.bank.customers.domain.service.UpdateCustomerCommand;
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
@Path("/customers/{id}")
public class CustomerResource {

    private CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = checkNotNull(customerService);
    }

    @GET
    public Response get(@PathParam("id") UUIDParam customerId) {
        Optional<Customer> possibleClient = customerService.loadCustomer(customerId.get());
        if (!possibleClient.isPresent()) return Response.status(NOT_FOUND).build();
        CustomerDto customerDto = toDto(possibleClient.get());
        return Response.ok(customerDto).build();
    }

    @PUT
    public Response put(@PathParam("id") UUIDParam customerId, @Valid @NotNull CustomerDto customerDto) {
        UpdateCustomerCommand command = new UpdateCustomerCommand(
                customerId.get(), customerDto.getName(), new Email(customerDto.getEmail()));
        customerService.process(command);
        return Response.noContent().build();
    }

    private CustomerDto toDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail().getValue());
        return dto;
    }
}
