package io.github.rdlopes.ledger.features.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.github.rdlopes.ledger.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class ParameterTypes {
  private static final Logger log = LoggerFactory.getLogger(ParameterTypes.class);

  private final ObjectMapper objectMapper;

  public ParameterTypes(ObjectMapper objectMapper) {
    log.trace("init, objectMapper: {}", objectMapper);

    this.objectMapper = objectMapper;
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
}
