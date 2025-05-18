package io.github.rdlopes.ledger.domain;

import java.util.UUID;

public record Customer(UUID id, String lastName, String firstName, String email) {
}
