package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.ing.model.transactions.Account;
import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import com.ing.transactions.engine.TransactionsCacheFactory;
import com.ing.transactions.engine.TransactionsEngine;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class TransactionsServiceTest {
    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private TransactionsCacheFactory transactionsCacheFactory;

    @Inject
    private TransactionsService transactionsService;


    @Test
    public void When_ProcessTransactions_Expect_ProcessEchTransactionAndSortByAccountName() {
        List<Transaction> transactions = easyRandom.objects(Transaction.class, COLLECTION_SIZE).toList();
        List<AccountDecorator> accounts = easyRandom.objects(AccountDecorator.class, COLLECTION_SIZE).toList();

        TransactionsEngine transactionsEngine = mock(TransactionsEngine.class);
        when(transactionsCacheFactory.transactionsCache()).thenReturn(transactionsEngine);
        when(transactionsEngine.getCurrentAccountsState()).thenReturn(accounts);

        ImmutableSortedSet<Account> result = transactionsService.processTransactions(transactions);


        verify(transactionsEngine, times(transactions.size())).processTransaction(any());
        transactions.forEach(
                transaction -> {
                    verify(transactionsEngine).processTransaction(transaction);
                }
        );
        result.forEach(account -> assertThat(accounts).contains((AccountDecorator) account));
        assertThat(Ordering.natural().onResultOf((Account::getAccount)).isOrdered(result)).isTrue();
    }

    @MockBean(TransactionsCacheFactory.class)
    TransactionsCacheFactory transactionsCacheFactory() {
        return mock(TransactionsCacheFactory.class);
    }

}
