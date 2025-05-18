package io.github.rdlopes.ledger.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a movement (transaction) on an account.
 */
public class Movement {
    private final Instant timestamp;
    private final String iban;
    private final Money amount;

    /**
     * Creates a new movement.
     *
     * @param timestamp the timestamp of the movement
     * @param iban      the IBAN of the account
     * @param amount    the amount of the movement
     */
    public Movement(Instant timestamp, String iban, Money amount) {
        this.timestamp = timestamp;
        this.iban = iban;
        this.amount = amount;
    }

    /**
     * Gets the timestamp of the movement.
     *
     * @return the timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the IBAN of the account.
     *
     * @return the IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * Gets the amount of the movement.
     *
     * @return the amount
     */
    public Money getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(timestamp, movement.timestamp) &&
                Objects.equals(iban, movement.iban) &&
                Objects.equals(amount, movement.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, iban, amount);
    }

    @Override
    public String toString() {
        return "Movement{" +
                "timestamp=" + timestamp +
                ", iban='" + iban + '\'' +
                ", amount=" + amount +
                '}';
    }
}
