package io.github.rdlopes.ledger.config;

import io.github.rdlopes.ledger.LedgerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeaturesConfiguration {
  private static final Logger log = LoggerFactory.getLogger(FeaturesConfiguration.class);

  public FeaturesConfiguration(LedgerProperties ledgerProperties) {
    log.trace("init - ledgerProperties: {}", ledgerProperties);
  }

}
