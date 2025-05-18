package io.github.rdlopes.ledger.features.config;

import io.cucumber.spring.ScenarioScope;
import io.github.rdlopes.ledger.application.LedgerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.context.annotation.ScopedProxyMode.NO;

@Configuration
public class FeaturesConfiguration {
  private static final Logger log = LoggerFactory.getLogger(FeaturesConfiguration.class);

  public FeaturesConfiguration(LedgerProperties ledgerProperties) {
    log.trace("init - ledgerProperties: {}", ledgerProperties);
  }

  @Bean
  @ScenarioScope(proxyMode = NO)
  public World world() {
    log.trace("world");
    return new World();
  }

}
