package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.ing.model.transactions.Account;
import com.ing.model.transactions.Transaction;
import com.ing.transactions.engine.TransactionsEngine;
import com.ing.transactions.engine.TransactionsCacheFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class TransactionsService {

    @Inject
    private TransactionsCacheFactory transactionsCacheFactory;

    public ImmutableSortedSet<Account> processTransactions(List<Transaction> transactions) {
        TransactionsEngine transactionsEngine = transactionsCacheFactory.transactionsCache();
        transactions.forEach(transactionsEngine::processTransaction);

        return ImmutableSortedSet.copyOf(Ordering.natural().onResultOf((Account::getAccount)),
                        transactionsEngine.getCurrentAccountsState());
    }
}
