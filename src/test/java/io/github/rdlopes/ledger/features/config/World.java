package io.github.rdlopes.ledger.features.config;

import io.github.rdlopes.ledger.domain.Account;
import io.github.rdlopes.ledger.domain.Customer;
import io.github.rdlopes.ledger.domain.Movement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class World {
  private static final Logger log = LoggerFactory.getLogger(World.class);
  private List<Customer> customers;
  private List<Account> accounts;
  private List<Movement> movements;

  public World() {
    this.customers = new ArrayList<>();
    this.accounts = new ArrayList<>();
    this.movements = new ArrayList<>();
  }

  public void setCustomers(List<Customer> customers) {
    log.trace("setCustomers - customers: {}", customers);
    this.customers = customers;
  }

  public List<Customer> getCustomers() {
    log.trace("getCustomers");
    return customers;
  }

  public Optional<Customer> findCustomerByFirstName(String firstName) {
    log.trace("findCustomerByFirstName - firstName: {}", firstName);
    return customers.stream()
            .filter(customer -> customer.firstName().equals(firstName))
            .findFirst();
  }

  public Optional<Customer> findCustomerById(UUID id) {
    log.trace("findCustomerById - id: {}", id);
    return customers.stream()
            .filter(customer -> customer.id().equals(id))
            .findFirst();
  }

  public void addAccount(Account account) {
    log.trace("addAccount - account: {}", account);
    accounts.add(account);
  }

  public void addAccounts(List<Account> accounts) {
    log.trace("addAccounts - accounts: {}", accounts);
    this.accounts.addAll(accounts);
  }

  public List<Account> getAccounts() {
    log.trace("getAccounts");
    return accounts;
  }

  public List<Account> getAccountsForCustomer(Customer customer) {
    log.trace("getAccountsForCustomer - customer: {}", customer);
    return accounts.stream()
            .filter(account -> account.getCustomer().equals(customer))
            .toList();
  }

  public Optional<Account> findAccountByIban(String iban) {
    log.trace("findAccountByIban - iban: {}", iban);
    return accounts.stream()
            .filter(account -> account.getIban().equals(iban))
            .findFirst();
  }

  public void addMovement(Movement movement) {
    log.trace("addMovement - movement: {}", movement);
    movements.add(movement);
  }

  public void addMovements(List<Movement> movements) {
    log.trace("addMovements - movements: {}", movements);
    this.movements.addAll(movements);
  }

  public List<Movement> getMovements() {
    log.trace("getMovements");
    return movements;
  }

  public List<Movement> getMovementsForAccount(String iban) {
    log.trace("getMovementsForAccount - iban: {}", iban);
    return movements.stream()
            .filter(movement -> movement.getIban().equals(iban))
            .toList();
  }
}
