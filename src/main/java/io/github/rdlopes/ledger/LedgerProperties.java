package io.github.rdlopes.ledger;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ledger")
public record LedgerProperties() {
}
