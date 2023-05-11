package com.ing.transactions.engine;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.UUID;

@Factory
public class TransactionsCacheFactory {

    @Inject
    private TransactionsOperations transactionsOperations;

    @Singleton
    public TransactionsEngine transactionsCache() {
        return new TransactionsEngine(transactionsOperations);
    };
}
