package io.github.rdlopes.ledger.features.config;

import io.github.rdlopes.ledger.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class World {
  private static final Logger log = LoggerFactory.getLogger(World.class);
  private List<Customer> customers;

  public void setCustomers(List<Customer> customers) {
    log.trace("setCustomers - customers: {}", customers);
    this.customers = customers;
  }

  public List<Customer> getCustomers() {
    log.trace("getCustomers");
    return customers;
  }
}
