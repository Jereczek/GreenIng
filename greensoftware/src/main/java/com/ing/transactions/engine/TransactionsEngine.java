package com.ing.transactions.engine;

import com.ing.atms.AtmsController;
import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TransactionsEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtmsController.class);
    private final String engineId;
    private long operations = 0;
    private final TransactionsOperations transactionsOperations;
    private final Map<Integer, AccountDecorator> accounts = new ConcurrentHashMap<>();

    public TransactionsEngine(TransactionsOperations transactionsOperations, String engineId) {
        LOGGER.info("Started transactions engine: {}", engineId);
        this.engineId = engineId;
        this.transactionsOperations = transactionsOperations;
    }

    public Collection<AccountDecorator> getCurrentAccountsState() {
        LOGGER.info("Current State for engine: {}", engineId);
        LOGGER.info("Operations count: {}", operations);
        return accounts.values();
    }

    public void processTransaction(Transaction transactionToProcess) {
        operations++;
        Integer debitKey =  Objects.hash(transactionToProcess.getDebitAccount());
        Integer creditKey = Objects.hash(transactionToProcess.getCreditAccount());

        accounts.merge(
                debitKey,
                transactionsOperations.registerNewDebitAccount(transactionToProcess),
                transactionsOperations::mergeAccounts);
        accounts.merge(
                creditKey,
                transactionsOperations.registerNewCreditAccount(transactionToProcess),
                transactionsOperations::mergeAccounts);
    }
}
