package com.bisnode.versioncheck;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import com.bisnode.versioncheck.listener.VersionCheckReportRenderer;
import com.bisnode.versioncheck.rules.DuplicateRule;
import com.bisnode.versioncheck.rules.SameVersionGroupRule;

public class VersionCheckPluginTest {

    @Test
    public void pluginSetupFunctional() {
        Project project = ProjectBuilder.builder().build();

        project.getPluginManager().apply("java");
        project.getPluginManager().apply("com.bisnode.version-check");
        project.getRepositories().mavenCentral();

        project.getDependencies().add("compile", new DefaultExternalModuleDependency("org.springframework.boot", "spring-boot-starter", "1.5.1.RELEASE"));
        project.getDependencies().add("testCompile", new DefaultExternalModuleDependency("org.springframework.boot", "spring-boot-starter-test", "1.4.1.RELEASE"));

        VersionCheckExtension extension = project.getExtensions().findByType(VersionCheckExtension.class);
        extension.setDependencies("transitive");

        extension.configurations("testCompile");

        extension.getVersionRules().add(new DuplicateRule());
        extension.getVersionRules().add(new SameVersionGroupRule("org.springframework.boot"));

        VersionCheckReportTask validateTask = (VersionCheckReportTask) project.getTasksByName("versionCheckReport", false).iterator().next();

        VersionCheckReportRenderer mockRenderer = mock(VersionCheckReportRenderer.class);
        validateTask.setRenderer(mockRenderer);

        validateTask.execute();

        verify(mockRenderer).startProject(project);
        verify(mockRenderer).completeProject(project);

        Configuration configuration = project.getConfigurations().findByName("testCompile");

        verify(mockRenderer).startConfiguration(configuration);
        verify(mockRenderer).completeConfiguration(configuration);

        verify(mockRenderer, atLeast(1)).startViolationGroup(any());
        verify(mockRenderer, atLeast(1)).completeViolationGroup();

        verify(mockRenderer, atLeast(1)).reportViolation(any(), anyVararg());

    }
}
