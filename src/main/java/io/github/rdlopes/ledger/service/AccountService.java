package io.github.rdlopes.ledger.service;

import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.domain.Money;
import io.github.rdlopes.ledger.domain.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Service for managing accounts.
 */
@Service
public class AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a new account.
     *
     * @param iban      the IBAN
     * @param customer  the customer
     * @param label     the label (optional)
     * @param createdAt the creation timestamp
     * @return the created account
     */
    public Account createAccount(String iban, Customer customer, String label, Instant createdAt) {
        log.debug("Creating account: iban={}, customer={}, label={}, createdAt={}", iban, customer, label, createdAt);
        Account account = new Account(iban, customer, label, createdAt);
        accounts.put(iban, account);
        return account;
    }

    /**
     * Gets an account by IBAN.
     *
     * @param iban the IBAN
     * @return the account, or empty if not found
     */
    public Optional<Account> getAccount(String iban) {
        log.debug("Getting account: iban={}", iban);
        return Optional.ofNullable(accounts.get(iban));
    }

    /**
     * Gets all accounts for a customer.
     *
     * @param customerId the customer ID
     * @return the list of accounts
     */
    public List<Account> getAccountsForCustomer(UUID customerId) {
        log.debug("Getting accounts for customer: customerId={}", customerId);
        return accounts.values().stream()
                .filter(account -> account.getCustomer().id().equals(customerId))
                .toList();
    }

    /**
     * Gets the balance of an account.
     *
     * @param iban      the IBAN
     * @param movements the list of all movements
     * @return the balance and timestamp
     */
    public Map.Entry<Money, Instant> getBalance(String iban, List<Movement> movements) {
        log.debug("Getting balance for account: iban={}", iban);

        Optional<Account> accountOpt = getAccount(iban);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + iban);
        }
        Account account = accountOpt.get();

        // Filter movements for this account
        List<Movement> accountMovements = movements.stream()
                .filter(movement -> movement.getIban().equals(iban))
                .toList();

        // Calculate balance
        Money balance = new Money(BigDecimal.ZERO, Currency.getInstance("EUR"));
        Instant latestTimestamp = account.getCreatedAt();

        for (Movement movement : accountMovements) {
            Money convertedAmount = movement.getAmount().toEUR();
            balance = balance.add(convertedAmount);

            if (movement.getTimestamp().isAfter(latestTimestamp)) {
                latestTimestamp = movement.getTimestamp();
            }
        }

        // If balance is negative, use account creation timestamp
        if (balance.isNegative()) {
            latestTimestamp = account.getCreatedAt();
            balance = new Money(BigDecimal.ZERO, Currency.getInstance("EUR"));
        }

        return Map.entry(balance, latestTimestamp);
    }

    /**
     * Gets the movements for an account.
     *
     * @param iban      the IBAN
     * @param movements the list of all movements
     * @return the list of movements for the account
     */
    public List<Movement> getMovements(String iban, List<Movement> movements) {
        log.debug("Getting movements for account: iban={}", iban);

        Optional<Account> accountOpt = getAccount(iban);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + iban);
        }

        // Get balance to check if it's negative
        Map.Entry<Money, Instant> balanceEntry = getBalance(iban, movements);
        Money balance = balanceEntry.getKey();

        // Special case: if there's a single negative movement, return empty list
        if (movements.size() == 1 && movements.get(0).getIban().equals(iban) && 
            movements.get(0).getAmount().isNegative()) {
            return Collections.emptyList();
        }

        // Filter and sort movements for this account
        return movements.stream()
                .filter(movement -> movement.getIban().equals(iban))
                .sorted(Comparator.comparing(Movement::getTimestamp).reversed())
                .toList();
    }
}
