package com.ing.transactions.engine;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MicronautTest
public class TransactionsCacheFactoryTest {
    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private TransactionsOperations transactionsOperations;

    @Inject
    private TransactionsCacheFactory transactionsCacheFactory;

    @Test
    public void When_ClanPickerEngineFactoryBuildClanPicker_Expect_NewEngineReturned() {
        TransactionsEngine transactionsEngine1 = transactionsCacheFactory.transactionsCache();
        TransactionsEngine transactionsEngine2 = transactionsCacheFactory.transactionsCache();

        assertThat(transactionsEngine1).isNotEqualTo(transactionsEngine2);
    }

    @MockBean(TransactionsOperations.class)
    TransactionsOperations transactionsOperations() {
        return mock(TransactionsOperations.class);
    }
}
