package com.ing.transactions.engine;

import com.ing.model.transactions.Transaction;
import com.ing.transactions.decorators.AccountDecorator;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.Objects;

@Singleton
public class TransactionsOperations {
    private final static BigDecimal CURRENCY_DIVIDER = BigDecimal.valueOf(10); // PLN/EUR/USD
    private final static int DEFAULT_STARTING_POINT_COUNTER = 1;
    private final static int DEFAULT_EMPTY_COUNTER = 0;

    public AccountDecorator mergeAccounts(AccountDecorator currentAccountState, AccountDecorator newAccount) {
        if (!Objects.equals(currentAccountState.getAccount(), newAccount.getAccount())) {
            throw new RuntimeException("FATAL: Business Logic! Cannot merge two accounts as there is different iban");
        }
        currentAccountState.setDebitCount(sumCount(currentAccountState.getDebitCount(), newAccount.getDebitCount()));
        currentAccountState.setCreditCount(sumCount(currentAccountState.getCreditCount(), newAccount.getCreditCount()));
        currentAccountState.setBalanceInCents(sumBalance(currentAccountState.getBalanceInCents(), newAccount.getBalanceInCents()));
        return currentAccountState;
    }

    public AccountDecorator registerNewCreditAccount(Transaction creditTransaction) {
        return new AccountDecorator(
                creditTransaction.getCreditAccount(),
                DEFAULT_EMPTY_COUNTER,
                DEFAULT_STARTING_POINT_COUNTER,
                getCentsFromAmount(Objects.requireNonNull(creditTransaction.getAmount()))
        );
    }

    public AccountDecorator registerNewDebitAccount(Transaction debitTransaction) {
        return new AccountDecorator(
                debitTransaction.getDebitAccount(),
                DEFAULT_STARTING_POINT_COUNTER,
                DEFAULT_EMPTY_COUNTER,
                getCentsFromAmount(Objects.requireNonNull(debitTransaction.getAmount()).negate())
        );
    }

    private long sumBalance(long balance1, long balance2) {
        return balance1 + balance2;
    }

    private Integer sumCount(Integer count1, Integer count2) {
        return count1 + count2;
    }

    private long getCentsFromAmount(BigDecimal amount) {
        return amount.multiply(CURRENCY_DIVIDER).longValue();
    }
}
