package kboom.playground.bank.transactions.domain.service;

import java.util.UUID;

import static java.lang.String.format;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(UUID id) {
        super(format("Transaction with id '%s' could not be found", id));
    }

}
