package io.github.rdlopes.ledger.config;

import io.cucumber.spring.CucumberContextConfiguration;
import io.github.rdlopes.ledger.LedgerApplication;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = LedgerApplication.class)
@AutoConfigureJson
@AutoConfigureMockMvc
@ActiveProfiles("features")
public class CucumberConfiguration {
}
