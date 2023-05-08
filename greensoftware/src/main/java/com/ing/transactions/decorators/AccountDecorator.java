package com.ing.transactions.decorators;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ing.model.transactions.Account;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountDecorator extends Account {
    private final static BigDecimal CURRENCY_DIVIDER = BigDecimal.valueOf(10); // PLN/EUR/USD

    @JsonIgnore
    long balanceInCents;

    public AccountDecorator(String account, Integer debitCount, Integer creditCount, long balanceInCents) {
        super();
        this.setAccount(account);
        this.setDebitCount(debitCount);
        this.setCreditCount(creditCount);
        this.balanceInCents = balanceInCents;
    }

    @Nullable
    @Override
    public BigDecimal getBalance() {
        return BigDecimal.valueOf(balanceInCents)
                .divide(CURRENCY_DIVIDER,2, RoundingMode.UNNECESSARY);
    }

    public void setBalanceInCents(long balanceInCents) {
        this.balanceInCents = balanceInCents;
    }

    public long getBalanceInCents() {
        return balanceInCents;
    }

}
