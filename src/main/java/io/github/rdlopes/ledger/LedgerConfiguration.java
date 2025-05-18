package io.github.rdlopes.ledger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LedgerProperties.class)
public class LedgerConfiguration {

}
