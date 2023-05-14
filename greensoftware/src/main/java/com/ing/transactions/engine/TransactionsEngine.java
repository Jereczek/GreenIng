package com.ing.transactions.engine;

import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TransactionsEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsEngine.class);
    private final String engineId = String.valueOf(UUID.randomUUID());
    private final TransactionsOperations transactionsOperations;
    private final Map<Integer, AccountDecorator> accounts = new ConcurrentHashMap<>();

    public TransactionsEngine(TransactionsOperations transactionsOperations) {
        LOGGER.debug("EngineId: {} | TransactionsEngine initialized.", engineId);
        this.transactionsOperations = transactionsOperations;
    }

    public Collection<AccountDecorator> getCurrentAccountsState() {
        return accounts.values();
    }

    public void processTransaction(Transaction transactionToProcess) {
        LOGGER.debug("EngineId: {} | TransactionsEngine - processing transaction: {}.",
                engineId, transactionToProcess);
        Integer debitKey =  Objects.hash(transactionToProcess.getDebitAccount());
        Integer creditKey = Objects.hash(transactionToProcess.getCreditAccount());

        if (debitKey.equals(creditKey)) {
            return;
        }

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
