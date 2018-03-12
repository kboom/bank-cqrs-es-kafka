package kboom.playground.bank.customers.domain.service;


import kboom.playground.bank.customers.domain.model.Email;

import static com.google.common.base.Preconditions.checkNotNull;

public class EnrollClientCommand {

    private final String name;
    private final Email email;

    public EnrollClientCommand(String name, Email email) {
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
