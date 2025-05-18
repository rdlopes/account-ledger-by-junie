package io.github.rdlopes.ledger;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication(scanBasePackages = "io.github.rdlopes.ledger")
public class LedgerApplication {
  public static void main(String[] args) {
    run(LedgerApplication.class, args);
  }
  
}
