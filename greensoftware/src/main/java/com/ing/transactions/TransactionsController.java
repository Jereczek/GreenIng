package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.ing.model.transactions.Account;
import com.ing.model.transactions.Transaction;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.util.List;

import static io.micronaut.http.context.ServerRequestContext.with;

@Controller("/transactions")
public class TransactionsController {

    @Inject
    private TransactionsService transactionsService;

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            uri = "/report")
    public ImmutableSortedSet<Account> report(@Body List<Transaction> transactions) {
        return transactionsService.processTransactions(transactions);
    }
}
