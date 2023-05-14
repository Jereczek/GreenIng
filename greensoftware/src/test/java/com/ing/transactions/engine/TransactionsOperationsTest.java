package com.ing.transactions.engine;

import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class TransactionsOperationsTest {
    private final static int MAX_BOUND = 200000;
    private final static BigDecimal CURRENCY_DIVIDER = BigDecimal.valueOf(10); // PLN/EUR/USD

    private final EasyRandom easyRandom = new EasyRandom();

    @Inject
    private TransactionsOperations transactionsOperations;

    @Test
    public void When_RegisterNewCreditAccount_Expect_ProperCreditAccount() {
        Transaction creditTransaction = easyRandom.nextObject(Transaction.class);
        AccountDecorator creditAccount = transactionsOperations.registerNewCreditAccount(creditTransaction);

        assertThat(creditAccount).isNotNull();
        assertThat(creditAccount.getAccount()).isEqualTo(creditAccount.getAccount());
        assertThat(creditAccount.getCreditCount()).isEqualTo(1);
        assertThat(creditAccount.getDebitCount()).isEqualTo(0);
        assertThat(creditAccount.getBalanceInCents())
                .isEqualTo(creditTransaction.getAmount().multiply(CURRENCY_DIVIDER).longValue());
    }

    @Test
    public void When_RegisterNewDebitAccount_Expect_ProperDebitAccount() {
        Transaction creditTransaction = easyRandom.nextObject(Transaction.class);
        AccountDecorator creditAccount = transactionsOperations.registerNewDebitAccount(creditTransaction);

        assertThat(creditAccount).isNotNull();
        assertThat(creditAccount.getAccount()).isEqualTo(creditAccount.getAccount());
        assertThat(creditAccount.getCreditCount()).isEqualTo(0);
        assertThat(creditAccount.getDebitCount()).isEqualTo(1);
        assertThat(creditAccount.getBalanceInCents())
                .isEqualTo(creditTransaction.getAmount().multiply(CURRENCY_DIVIDER).negate().longValue());
    }

    @Test
    public void When_MergeTwoAccounts_Expect_ProperOutcomeFromMerge() {
        String sameAccount = easyRandom.nextObject(String.class);
        AccountDecorator accountDecorator1 = prepareAccount(sameAccount);
        AccountDecorator accountDecorator2 = prepareAccount(sameAccount);
        int expectedCreditCount = accountDecorator1.getCreditCount() + accountDecorator2.getCreditCount();
        int expectedDebitCount = accountDecorator1.getDebitCount() + accountDecorator2.getDebitCount();
        long expectedBalanceCents = accountDecorator1.getBalanceInCents() + accountDecorator2.getBalanceInCents();

        AccountDecorator result = transactionsOperations.mergeAccounts(accountDecorator1, accountDecorator2);

        assertThat(result).isSameAs(accountDecorator1);
        assertThat(result.getAccount()).isEqualTo(sameAccount);
        assertThat(result.getCreditCount()).isEqualTo(expectedCreditCount);
        assertThat(result.getDebitCount()).isEqualTo(expectedDebitCount);
        assertThat(result.getBalanceInCents()).isEqualTo(expectedBalanceCents);
    }

    private AccountDecorator prepareAccount(String accountName) {
        AccountDecorator accountDecorator = easyRandom.nextObject(AccountDecorator.class);
        accountDecorator.setAccount(accountName);
        accountDecorator.setCreditCount(easyRandom.nextInt(0, MAX_BOUND));
        accountDecorator.setDebitCount(easyRandom.nextInt(0, MAX_BOUND));
        return accountDecorator;
    }
}
