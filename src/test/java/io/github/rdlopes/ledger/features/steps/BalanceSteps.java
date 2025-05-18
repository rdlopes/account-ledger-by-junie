package io.github.rdlopes.ledger.features.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Lorsque;
import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.domain.Money;
import io.github.rdlopes.ledger.domain.Movement;
import io.github.rdlopes.ledger.features.config.World;
import io.github.rdlopes.ledger.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceSteps {
    private static final Logger log = LoggerFactory.getLogger(BalanceSteps.class);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final World world;
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private String balanceResponse;

    public BalanceSteps(World world, ObjectMapper objectMapper) {
        this.world = world;
        this.objectMapper = objectMapper;
        this.accountService = new AccountService();

        // Register accounts from world in the service
        world.getAccounts().forEach(account -> 
                accountService.createAccount(
                        account.getIban(), 
                        account.getCustomer(), 
                        account.getLabel(), 
                        account.getCreatedAt()
                )
        );
    }

    @Etantdonné("qu'aucun mouvement n'est constaté")
    public void noMovementsRecorded() {
        log.debug("No movements recorded");
        // Nothing to do, movements list is already empty
    }

    @Etantdonnéque("aucun mouvement n'est constaté")
    public void noMovementsRecordedAlternative() {
        noMovementsRecorded();
    }

    @Etantdonné("que les mouvements suivants ont été constatés")
    public void movementsRecorded(List<Movement> movements) {
        log.debug("Movements recorded: {}", movements);
        world.addMovements(movements);
    }

    @Etantdonnéque("les mouvements suivants ont été constatés")
    public void movementsRecordedAlternative(List<Movement> movements) {
        movementsRecorded(movements);
    }

    @Lorsque("{word} requiert le solde du compte {}")
    public void customerRequestsAccountBalance(String customerFirstName, String iban) {
        log.debug("Customer requests account balance: {}, iban: {}", customerFirstName, iban);

        Customer customer = world.findCustomerByFirstName(customerFirstName)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerFirstName));

        Account account = world.findAccountByIban(iban)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + iban));

        // Verify the account belongs to the customer
        if (!account.getCustomer().equals(customer)) {
            throw new IllegalArgumentException("Account " + iban + " does not belong to customer " + customerFirstName);
        }

        // Get balance
        Map.Entry<Money, Instant> balanceEntry = accountService.getBalance(iban, world.getMovements());
        Money balance = balanceEntry.getKey();
        Instant timestamp = balanceEntry.getValue();

        // Create response in the expected format
        Map<String, Object> response = Map.of(
                "iban", iban,
                "amount", balance.format(),
                "timestamp", timestamp.atOffset(ZoneOffset.UTC).format(ISO_FORMATTER)
        );

        try {
            balanceResponse = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize balance response", e);
        }
    }

    @Alors("le solde retourné doit être")
    public void balanceShouldBe(String expectedJson) {
        log.debug("Balance should be: {}", expectedJson);

        try {
            // Parse both JSONs to compare them regardless of formatting and field order
            Object expected = objectMapper.readValue(expectedJson, Object.class);
            Object actual = objectMapper.readValue(balanceResponse, Object.class);

            // Convert to maps for comparison
            Map<String, Object> expectedMap = objectMapper.convertValue(expected, Map.class);
            Map<String, Object> actualMap = objectMapper.convertValue(actual, Map.class);

            assertEquals(expectedMap, actualMap, "Balance response does not match expected JSON");
        } catch (Exception e) {
            throw new RuntimeException("Failed to compare JSON responses", e);
        }
    }
}
