package com.plgrnds.bank.customers.domain.service;


import com.plgrnds.bank.customers.domain.model.Email;

import static com.google.common.base.Preconditions.checkNotNull;

public class EnrollCustomerCommand {

    private final String name;
    private final Email email;

    public EnrollCustomerCommand(String name, Email email) {
        this.name = checkNotNull(name);
        this.email = checkNotNull(email);
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}
