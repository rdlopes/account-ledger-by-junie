package io.github.rdlopes.ledger.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a monetary amount with a specific currency.
 */
public class Money {
    private static final Locale FRENCH_LOCALE = Locale.FRANCE;
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");
    private static final BigDecimal USD_TO_EUR_RATE = new BigDecimal("0.8");

    private final BigDecimal amount;
    private final Currency currency;

    /**
     * Creates a new monetary amount.
     *
     * @param amount   the amount
     * @param currency the currency
     */
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    /**
     * Parses a monetary amount from a string in the format "100,00 EUR".
     *
     * @param moneyString the string to parse
     * @return the parsed Money object
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    public static Money parse(String moneyString) {
        try {
            String[] parts = moneyString.trim().split(" ");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid money format: " + moneyString);
            }

            String amountStr = parts[0];
            String currencyStr = parts[1];

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(FRENCH_LOCALE);
            DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
            format.setParseBigDecimal(true);

            BigDecimal amount = (BigDecimal) format.parse(amountStr);
            Currency currency = Currency.getInstance(currencyStr);

            return new Money(amount, currency);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse money: " + moneyString, e);
        }
    }

    /**
     * Converts this money to EUR if it's in a different currency.
     *
     * @return the money in EUR
     */
    public Money toEUR() {
        if (EUR.equals(currency)) {
            return this;
        }

        if (USD.equals(currency)) {
            return new Money(amount.multiply(USD_TO_EUR_RATE), EUR);
        }

        throw new UnsupportedOperationException("Conversion from " + currency + " to EUR not supported");
    }

    /**
     * Adds another money amount to this one.
     *
     * @param other the money to add
     * @return the sum
     * @throws IllegalArgumentException if the currencies don't match
     */
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(amount.add(other.amount), currency);
    }

    /**
     * Checks if this money amount is negative.
     *
     * @return true if the amount is negative, false otherwise
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Formats this money as a string in the format "100,00 EUR".
     *
     * @return the formatted string
     */
    public String format() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(FRENCH_LOCALE);
        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        return format.format(amount) + " " + currency.getCurrencyCode();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return format();
    }
}
