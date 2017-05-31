package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.List;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.junit.Test;
import org.mockito.Mockito;

import com.bisnode.versioncheck.rules.DuplicateRule;

public class DuplicateRuleTest {

    @Test
    public void check() {
        List<ModuleVersionIdentifier> deps = Arrays.asList(
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot-starter-test", "1.4.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.5.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.4.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.3.1")
                );

        DuplicateRule rule = new DuplicateRule();
        Configuration config = Mockito.mock(Configuration.class);
        Mockito.when(config.getName()).thenReturn("myConfigName");

        ValidationReportRenderer renderer = Mockito.mock(ValidationReportRenderer.class);

        rule.apply(config, deps, renderer);
    }
}
