package io.github.rdlopes.ledger.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a bank account in the ledger system.
 */
public class Account {
    private final String iban;
    private final Customer customer;
    private final String label;
    private final Instant createdAt;

    /**
     * Creates a new account.
     *
     * @param iban      the International Bank Account Number
     * @param customer  the account owner
     * @param label     the account label (optional)
     * @param createdAt the account creation timestamp
     */
    public Account(String iban, Customer customer, String label, Instant createdAt) {
        this.iban = iban;
        this.customer = customer;
        this.label = Objects.requireNonNullElse(label, "Compte courant");
        this.createdAt = createdAt;
    }

    public String getIban() {
        return iban;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getLabel() {
        return label;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(iban, account.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return "Account{" +
                "iban='" + iban + '\'' +
                ", customer=" + customer +
                ", label='" + label + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
