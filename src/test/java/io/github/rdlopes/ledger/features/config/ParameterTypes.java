package io.github.rdlopes.ledger.features.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.domain.Money;
import io.github.rdlopes.ledger.domain.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ParameterTypes {
  private static final Logger log = LoggerFactory.getLogger(ParameterTypes.class);
  private static final DateTimeFormatter FRENCH_DATE_TIME_FORMATTER = 
      DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm:ss", Locale.FRANCE);

  private final ObjectMapper objectMapper;
  private final World world;

  public ParameterTypes(ObjectMapper objectMapper, World world) {
    log.trace("init, objectMapper: {}, world: {}", objectMapper, world);

    this.objectMapper = objectMapper;
    this.world = world;
  }

  @DefaultParameterTransformer
  @DefaultDataTableEntryTransformer
  @DefaultDataTableCellTransformer
  public Object transformer(Object fromValue, Type toValueType) {
    log.trace("transformer - fromValue: {}, toValueType: {}", fromValue, toValueType);

    return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
  }

  @DataTableType
  public Customer customer(Map<String, String> entries) {
    log.trace("customer - entries: {}", entries);

    var id = entries.get("Identifiant");
    var lastName = entries.get("Nom de famille");
    var firstName = entries.get("Prénom");
    var email = entries.get("E-mail");

    return new Customer(UUID.fromString(id), lastName, firstName, email);
  }

  @DataTableType
  public Account account(Map<String, String> entries) {
    log.trace("account - entries: {}", entries);

    var iban = entries.get("IBAN");
    var customerFirstName = entries.get("Client");
    var label = entries.get("Libellé");
    var createdAtStr = entries.get("Créé le");

    // Find customer by first name
    Customer customer = world.findCustomerByFirstName(customerFirstName)
        .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerFirstName));

    // Parse creation date
    LocalDateTime localDateTime = LocalDateTime.parse(createdAtStr, FRENCH_DATE_TIME_FORMATTER);
    Instant createdAt = localDateTime.toInstant(ZoneOffset.UTC);

    return new Account(iban, customer, label, createdAt);
  }

  @DataTableType
  public Movement movement(Map<String, String> entries) {
    log.trace("movement - entries: {}", entries);

    var timestampStr = entries.get("Instant");
    var iban = entries.get("IBAN");
    var amountStr = entries.get("Montant");

    // Parse timestamp
    LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, FRENCH_DATE_TIME_FORMATTER);
    Instant timestamp = localDateTime.toInstant(ZoneOffset.UTC);

    // Parse amount
    Money amount = Money.parse(amountStr);

    return new Movement(timestamp, iban, amount);
  }
}
