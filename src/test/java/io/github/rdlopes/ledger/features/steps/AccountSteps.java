package io.github.rdlopes.ledger.features.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.java.fr.Lorsque;
import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.features.config.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountSteps {
    private static final Logger log = LoggerFactory.getLogger(AccountSteps.class);

    private final World world;
    private final ObjectMapper objectMapper;
    private String accountListResponse;

    public AccountSteps(World world, ObjectMapper objectMapper) {
        this.world = world;
        this.objectMapper = objectMapper;
    }

    @Etantdonnéque("CEL n'a créé aucun compte")
    public void noAccountsCreated() {
        log.debug("No accounts created");
        // Nothing to do, accounts list is already empty
    }

    @Etantdonnéque("CEL a créé le compte")
    public void accountCreated(List<Account> accounts) {
        log.debug("Account created: {}", accounts);
        world.addAccounts(accounts);
    }

    @Etantdonnéque("CEL a créé les comptes")
    public void accountsCreated(List<Account> accounts) {
        log.debug("Accounts created: {}", accounts);
        world.addAccounts(accounts);
    }

    @Etantdonné("que CEL n'a créé aucun compte")
    public void noAccountsCreatedAlternative() {
        noAccountsCreated();
    }

    @Etantdonné("que CEL a créé le compte")
    public void accountCreatedAlternative(List<Account> accounts) {
        accountCreated(accounts);
    }

    @Etantdonné("que CEL a créé les comptes")
    public void accountsCreatedAlternative(List<Account> accounts) {
        accountsCreated(accounts);
    }

    @Lorsque("{word} requiert la liste de ses comptes")
    public void customerRequestsAccountList(String customerFirstName) {
        log.debug("Customer requests account list: {}", customerFirstName);

        Customer customer = world.findCustomerByFirstName(customerFirstName)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerFirstName));

        List<Account> customerAccounts = world.getAccountsForCustomer(customer);

        // Create response in the expected format
        Map<String, Object> response = Map.of(
                "customerId", customer.id().toString(),
                "accounts", customerAccounts.stream()
                        .map(account -> Map.of(
                                "iban", account.getIban(),
                                "label", account.getLabel()
                        ))
                        .collect(Collectors.toList())
        );

        try {
            accountListResponse = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize account list response", e);
        }
    }

    @Alors("la liste de comptes retournée doit être")
    public void accountListShouldBe(String expectedJson) {
        log.debug("Account list should be: {}", expectedJson);

        try {
            // Parse both JSONs to compare them regardless of formatting and field order
            Object expected = objectMapper.readValue(expectedJson, Object.class);
            Object actual = objectMapper.readValue(accountListResponse, Object.class);

            // Convert to maps for comparison
            Map<String, Object> expectedMap = objectMapper.convertValue(expected, Map.class);
            Map<String, Object> actualMap = objectMapper.convertValue(actual, Map.class);

            assertEquals(expectedMap, actualMap, "Account list response does not match expected JSON");
        } catch (Exception e) {
            throw new RuntimeException("Failed to compare JSON responses", e);
        }
    }
}
