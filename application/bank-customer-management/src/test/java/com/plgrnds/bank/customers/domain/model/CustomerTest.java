package com.plgrnds.bank.customers.domain.model;

import com.plgrnds.bank.commons.Event;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class CustomerTest {

    @Test
    public void newClientHasBeenEnrolled() throws Exception {
        UUID id = randomUUID();
        String name = "john";
        Email email = new Email("john@example.com");

        Customer customer = new Customer(id, name, email);

        List<Event> newEvents = customer.getNewEvents();
        assertThat(newEvents.size(), equalTo(1));
        assertThat(newEvents.get(0), instanceOf(CustomerEnrolledEvent.class));
        CustomerEnrolledEvent event = (CustomerEnrolledEvent) newEvents.get(0);
        assertThat(event.getAggregateId(), equalTo(id));
        assertThat(event.getName(), equalTo(name));
        assertThat(event.getEmail(), equalTo(email));

        assertThat(customer.getId(), equalTo(id));
        assertThat(customer.getName(), equalTo(name));
        assertThat(customer.getEmail(), equalTo(email));
    }

}
