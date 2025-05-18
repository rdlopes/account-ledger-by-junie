package io.github.rdlopes.ledger.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SimpleTest {

    @Test
    public void contextLoads() {
        // This test will pass if the Spring context loads successfully
        assertTrue(true, "Spring context loaded successfully");
    }
}
