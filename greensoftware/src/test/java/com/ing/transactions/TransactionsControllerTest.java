package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.ing.model.transactions.Transaction;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MicronautTest
public class TransactionsControllerTest {
    private static final int COLLECTION_SIZE = 3;
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private TransactionsService transactionsService;

    @Inject
    private TransactionsController transactionsController;

    @Test
    public void When_TransactionsControllerCalled_Expect_TransactionsServiceExecuted() {
        List<Transaction> transactions = easyRandom.objects(Transaction.class, COLLECTION_SIZE).toList();
        ImmutableSortedSet accounts = mock(ImmutableSortedSet.class);

        when(transactionsController.report(transactions)).thenReturn(accounts);

        ImmutableSortedSet result =  transactionsController.report(transactions);
        verify(transactionsService).processTransactions(transactions);
        assertThat(result).isEqualTo(accounts);
    }

    @Test
    public void When_TransactionsControllerCalled_And_TransactionsServiceThrowUncheckException_Expect_Rethrow() {
        List<Transaction> transactions = easyRandom.objects(Transaction.class, COLLECTION_SIZE).toList();
        when(transactionsService.processTransactions(transactions)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> transactionsController.report(transactions));
    }

    @MockBean(TransactionsService.class)
    TransactionsService transactionsService() {
        return mock(TransactionsService.class);
    }
}
