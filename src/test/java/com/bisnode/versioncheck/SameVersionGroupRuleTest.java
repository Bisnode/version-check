package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.List;

import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.junit.Test;
import org.mockito.Mockito;

import com.bisnode.versioncheck.listener.VersionCheckListener;
import com.bisnode.versioncheck.listener.VersionCheckReportRenderer;
import com.bisnode.versioncheck.rules.SameVersionGroupRule;

public class SameVersionGroupRuleTest {

    @Test
    public void checkSingleConflict() {
        List<ModuleVersionIdentifier> deps = Arrays.asList(
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot-starter-test", "1.4.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.5.1"),
                new DefaultModuleVersionIdentifier("org.springframework.boot", "spring-boot", "1.4.1")
                );

        SameVersionGroupRule rule = new SameVersionGroupRule("org.springframework.boot");

        VersionCheckListener mockRenderer = Mockito.mock(VersionCheckReportRenderer.class);

        rule.apply(deps, mockRenderer);

        verify(mockRenderer).startViolationGroup("org.springframework.boot");
        verify(mockRenderer).completeViolationGroup();

        verify(mockRenderer, times(2)).reportViolation(any(), anyVararg());
    }
}
