package io.github.rdlopes.ledger.features.steps;

import io.cucumber.java.fr.Etantdonné;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.features.config.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SetupWorld {
  private static final Logger log = LoggerFactory.getLogger(SetupWorld.class);

  private final World world;

  public SetupWorld(World world) {
    this.world = world;
  }

  @Etantdonné("nos clients")
  public void ourCustomers(List<Customer> customers) {
    log.trace("ourCustomers - customers: {}", customers);

    world.setCustomers(customers);
  }

}
