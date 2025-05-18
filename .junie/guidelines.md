# Account Ledger Project Guidelines

This document provides essential information for developers working on the Account Ledger project.

## Main Guideline
This project needs to be maintained by other developers.

## Build/Configuration Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.8 or higher

### Building the Project
The project uses Maven for build management. To build the project:

```bash
# Clean and build the project
mvn clean install

# Build without running tests
mvn clean install -DskipTests
```

### Running the Application
The application is a Spring Boot application and can be run using:

```bash
# Run using Maven
mvn spring-boot:run

# Run the JAR file directly
java -jar target/account-ledger-by-junie-1.0-SNAPSHOT-exec.jar
```

## Testing Information

### Testing Framework
The project uses Cucumber for BDD (Behavior-Driven Development) testing with Spring Boot integration. 
The project uses Cukedoctor to generate the its documentation.
Key components:

- **Cucumber**: For writing and running BDD tests
- **Spring Boot Test**: For integration testing with Spring Boot
- **JUnit 5**: As the underlying test runner
- **Cukedoctor**: For generating the living documentation in French

### Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=SimpleTest

# Run Cucumber features
mvn verify
```

**Note**: Cucumber tests must be run through the LedgerFeatures test runner, not directly from feature files. The test runner is configured to find all feature files in the `src/test/resources/features` directory.

### Adding New Tests

#### JUnit Tests
1. Create a new test class in the `src/test/java/io/github/rdlopes/ledger/test` directory
2. Annotate with `@SpringBootTest` for integration tests
3. Use standard JUnit 5 annotations (`@Test`, etc.)

Example:
```java
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
```

#### Cucumber Tests
1. Create a feature file in `src/test/resources/features` directory
2. Write scenarios using Gherkin syntax in French language
3. Create step definitions in `src/test/java/io/github/rdlopes/ledger/features/steps` directory

Feature file example (`src/test/resources/features/01-liste-des-comptes.feature`):

Step definitions example (`src/test/java/io/github/rdlopes/ledger/features/steps/SetupWorld.java`):

```java
public class SetupWorld {
  private static final Logger log = LoggerFactory.getLogger(SetupWorld.class);

  private final World world;

  public SetupWorld(World world) {
    this.world = world;
  }

  @Etantdonné("nos clients")
  public void ourCustomers(List<Customer> customers) {
    log.trace("ourCustomers - customers: {}", customers);

    world.setCustomers(customers);
  }

}
```

### French Language Support

The project supports writing feature files in French. Use the following annotations for step definitions:

- `@Etantdonné` or `@Etantdonnéque` for "Given" steps
- `@Lorsque` for "When" steps
- `@Alors` for "Then" steps

## Documentation Generation

The project uses Cukedoctor to generate documentation from Cucumber features:

```bash
# Generate documentation
mvn cukedoctor:execute
```

The generated documentation will be available in `target/cukedoctor/index.html`.

## Additional Development Information

### Project Structure
- `src/main/java`: Main application code
- `src/main/resources`: Application configuration
- `src/test/java`: Test code
- `src/test/resources`: Test resources, including feature files
- `target`: Build output

**Note** a special file located at `src/test/resources/cukedoctor-intro.adoc`
gives context about the project and the domain.

### Code Style
The project uses the Maven Spotless plugin for code formatting. To format the code:

```bash
mvn spotless:apply
```

### Logging
The project uses SLF4J with Logback for logging. Log levels can be configured in `src/main/resources/application.yaml` or `src/test/resources/application-features.yaml` for tests.

### Spring Profiles
- `features`: Used for running Cucumber tests

### Important Classes
- `LedgerApplication`: Main application entry point
- `LedgerConfiguration`: Spring configuration
- `LedgerFeatures`: Cucumber test runner
- `CucumberConfiguration`: Cucumber-Spring integration configuration
- `FeaturesConfiguration`: Spring configuration for Cucumber tests
