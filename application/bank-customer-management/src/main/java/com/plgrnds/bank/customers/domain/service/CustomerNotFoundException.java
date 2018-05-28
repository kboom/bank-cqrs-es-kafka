package com.plgrnds.bank.customers.domain.service;

import java.util.UUID;

import static java.lang.String.format;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(UUID id) {
        super(format("Client with id '%s' could not be found", id));
    }
}
