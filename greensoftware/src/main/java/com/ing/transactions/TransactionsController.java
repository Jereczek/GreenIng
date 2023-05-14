package com.ing.transactions;

import com.google.common.collect.ImmutableSortedSet;
import com.ing.model.transactions.Account;
import com.ing.model.transactions.Transaction;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/transactions")
public class TransactionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);
    @Inject
    private TransactionsService transactionsService;

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            uri = "/report")
    public ImmutableSortedSet<Account> report(@Body List<Transaction> transactions) {
        LOGGER.info("Incoming request | POST | /transactions/report | Number of transactions {}", transactions.size());
        return transactionsService.processTransactions(transactions);
    }
}
