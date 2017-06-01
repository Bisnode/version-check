package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.List;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.junit.Test;
import org.mockito.Mockito;

import com.bisnode.versioncheck.listener.VersionCheckListener;
import com.bisnode.versioncheck.listener.VersionCheckReportRenderer;
import com.bisnode.versioncheck.rules.SameVersionGroupRule;

public class SameVersionGroupRuleTest {

    @Test
    public void check() {
        List<ModuleVersionIdentifier> deps = Arrays.asList(
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot-starter-test", "1.4.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.5.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.4.1")
                );

        SameVersionGroupRule rule = new SameVersionGroupRule("org.springframework.boot");
        Configuration config = Mockito.mock(Configuration.class);
        Mockito.when(config.getName()).thenReturn("myConfigName");

        VersionCheckListener renderer = Mockito.mock(VersionCheckReportRenderer.class);

        rule.apply(config, deps, renderer);
    }
}
