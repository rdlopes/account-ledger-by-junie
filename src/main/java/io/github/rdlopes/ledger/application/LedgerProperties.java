package io.github.rdlopes.ledger.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ledger")
public record LedgerProperties() {
}
