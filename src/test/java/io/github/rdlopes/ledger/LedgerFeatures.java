package io.github.rdlopes.ledger;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.rdlopes.ledger")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "json:target/cucumber/report.json")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not ( @WIP )")
public class LedgerFeatures {
}
