package com.plgrnds.bank.customers.command;

import com.plgrnds.bank.customers.domain.service.EnrollCustomerCommand;
import com.plgrnds.bank.customers.domain.model.Customer;
import com.plgrnds.bank.customers.domain.service.CustomerService;
import com.plgrnds.bank.customers.domain.model.Email;

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
@Path("/customers")
public class CustomersResource {

    private CustomerService customerService;

    public CustomersResource(CustomerService customerService) {
        this.customerService = checkNotNull(customerService);
    }

    @POST
    public Response post(@Valid CustomerDto newCustomerDto) {
        EnrollCustomerCommand enrollCustomerCommand = new EnrollCustomerCommand(
            newCustomerDto.getName(), new Email(newCustomerDto.getEmail()));
        Customer customer = customerService.process(enrollCustomerCommand);
        URI clientUri = fromResource(CustomerResource.class).build(customer.getId());
        return Response.created(clientUri).build();
    }
}
