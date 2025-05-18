package io.github.rdlopes.ledger.features.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Lorsque;
import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.domain.Movement;
import io.github.rdlopes.ledger.features.config.World;
import io.github.rdlopes.ledger.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovementSteps {
    private static final Logger log = LoggerFactory.getLogger(MovementSteps.class);
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    private final World world;
    private final ObjectMapper objectMapper;
    private final AccountService accountService;
    private String movementsResponse;

    public MovementSteps(World world, ObjectMapper objectMapper) {
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

    @Lorsque("{word} requiert la liste de mouvements sur le compte {}")
    public void customerRequestsMovementsList(String customerFirstName, String iban) {
        log.debug("Customer requests movements list: {}, iban: {}", customerFirstName, iban);

        Customer customer = world.findCustomerByFirstName(customerFirstName)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerFirstName));

        Account account = world.findAccountByIban(iban)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + iban));

        // Verify the account belongs to the customer
        if (!account.getCustomer().equals(customer)) {
            throw new IllegalArgumentException("Account " + iban + " does not belong to customer " + customerFirstName);
        }

        // Get movements
        List<Movement> movements = accountService.getMovements(iban, world.getMovements());

        // Create response in the expected format
        Map<String, Object> response = Map.of(
                "iban", iban,
                "movements", movements.stream()
                        .map(movement -> Map.of(
                                "amount", movement.getAmount().format(),
                                "timestamp", movement.getTimestamp().atOffset(ZoneOffset.UTC).format(ISO_FORMATTER)
                        ))
                        .collect(Collectors.toList())
        );

        try {
            movementsResponse = objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize movements response", e);
        }
    }

    @Alors("la liste de mouvements retournée doit être")
    public void movementsListShouldBe(String expectedJson) {
        log.debug("Movements list should be: {}", expectedJson);

        try {
            // Parse both JSONs to compare them regardless of formatting and field order
            Object expected = objectMapper.readValue(expectedJson, Object.class);
            Object actual = objectMapper.readValue(movementsResponse, Object.class);

            // Convert to maps for comparison
            Map<String, Object> expectedMap = objectMapper.convertValue(expected, Map.class);
            Map<String, Object> actualMap = objectMapper.convertValue(actual, Map.class);

            // For nested lists, we need to compare each element
            List<Map<String, Object>> expectedMovements = (List<Map<String, Object>>) expectedMap.get("movements");
            List<Map<String, Object>> actualMovements = (List<Map<String, Object>>) actualMap.get("movements");

            if (expectedMovements != null && actualMovements != null) {
                assertEquals(expectedMovements.size(), actualMovements.size(), "Number of movements does not match");

                // Sort both lists by timestamp to ensure consistent order
                expectedMovements.sort((m1, m2) -> ((String) m1.get("timestamp")).compareTo((String) m2.get("timestamp")));
                actualMovements.sort((m1, m2) -> ((String) m1.get("timestamp")).compareTo((String) m2.get("timestamp")));

                for (int i = 0; i < expectedMovements.size(); i++) {
                    assertEquals(expectedMovements.get(i), actualMovements.get(i), 
                            "Movement at index " + i + " does not match");
                }
            }

            // Compare the rest of the fields
            assertEquals(expectedMap.get("iban"), actualMap.get("iban"), "IBAN does not match");

        } catch (Exception e) {
            throw new RuntimeException("Failed to compare JSON responses", e);
        }
    }
}
