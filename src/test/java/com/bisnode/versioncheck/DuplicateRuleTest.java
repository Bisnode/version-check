package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.List;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.junit.Test;
import org.mockito.Mockito;

import com.bisnode.versioncheck.listener.VersionCheckListener;
import com.bisnode.versioncheck.listener.VersionCheckReportRenderer;
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

        VersionCheckListener renderer = Mockito.mock(VersionCheckReportRenderer.class);

        rule.apply(deps, renderer);
    }
}
