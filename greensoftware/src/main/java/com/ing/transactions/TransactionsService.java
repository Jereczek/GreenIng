package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.ing.model.transactions.Account;
import com.ing.model.transactions.Transaction;
import com.ing.transactions.engine.TransactionsEngine;
import com.ing.transactions.engine.TransactionsCacheFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class TransactionsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsService.class);

    @Inject
    private TransactionsCacheFactory transactionsCacheFactory;

    public ImmutableSortedSet<Account> processTransactions(List<Transaction> transactions) {
        LOGGER.debug("Processing of {} transactions started", transactions.size());
        TransactionsEngine transactionsEngine = transactionsCacheFactory.transactionsCache();
        transactions.forEach(transactionsEngine::processTransaction); // According to "NQ" model and few tests, parallel stream is not worthy here due to little Q
        LOGGER.debug("Processing of {} transactions finished, sorting accounts...", transactions.size());
        return ImmutableSortedSet.copyOf(
                Ordering.natural().onResultOf((Account::getAccount)), transactionsEngine.getCurrentAccountsState());
    }
}
