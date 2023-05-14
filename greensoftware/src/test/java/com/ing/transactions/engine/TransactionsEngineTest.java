package com.ing.transactions.engine;

import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MicronautTest
public class TransactionsEngineTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void When_ProcessTransaction_Expect_ProcessMergesOnCollection() {
        TransactionsOperations transactionsOperations = spy(TransactionsOperations.class);

        TransactionsEngine transactionsEngine = new TransactionsEngine(transactionsOperations);
        assertThat(transactionsEngine.getCurrentAccountsState()).isEmpty();

        Transaction transaction = easyRandom.nextObject(Transaction.class);
        transactionsEngine.processTransaction(transaction);

        Collection<AccountDecorator> stateAfterTransaction = transactionsEngine.getCurrentAccountsState();

        assertThat(stateAfterTransaction.size()).isEqualTo(2);
        verify(transactionsOperations).registerNewDebitAccount(transaction);
        verify(transactionsOperations).registerNewCreditAccount(transaction);
    }

    @Test
    public void When_ProcessTransactionAndTransactionToItself_Expect_DoNothing() {
        String sameAccount = easyRandom.nextObject(String.class);
        TransactionsOperations transactionsOperations = mock(TransactionsOperations.class);

        TransactionsEngine transactionsEngine = new TransactionsEngine(transactionsOperations);
        assertThat(transactionsEngine.getCurrentAccountsState()).isEmpty();

        Transaction transaction = easyRandom.nextObject(Transaction.class);
        transaction.setCreditAccount(sameAccount);
        transaction.setDebitAccount(sameAccount);

        transactionsEngine.processTransaction(transaction);

        assertThat(transactionsEngine.getCurrentAccountsState()).isEmpty();
        verifyNoInteractions(transactionsOperations);
    }

}
