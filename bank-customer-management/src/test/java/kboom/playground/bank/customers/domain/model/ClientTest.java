package kboom.playground.bank.customers.domain.model;

import kboom.playground.bank.commons.Event;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ClientTest {

    @Test
    public void newClientHasBeenEnrolled() throws Exception {
        UUID id = randomUUID();
        String name = "john";
        Email email = new Email("john@example.com");

        Client client = new Client(id, name, email);

        List<Event> newEvents = client.getNewEvents();
        assertThat(newEvents.size(), equalTo(1));
        assertThat(newEvents.get(0), instanceOf(ClientEnrolledEvent.class));
        ClientEnrolledEvent event = (ClientEnrolledEvent) newEvents.get(0);
        assertThat(event.getAggregateId(), equalTo(id));
        assertThat(event.getName(), equalTo(name));
        assertThat(event.getEmail(), equalTo(email));

        assertThat(client.getId(), equalTo(id));
        assertThat(client.getName(), equalTo(name));
        assertThat(client.getEmail(), equalTo(email));
    }

}
