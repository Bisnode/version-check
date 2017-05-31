package com.bisnode.versioncheck;

import static org.mockito.Mockito.*;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;
import org.mockito.Mockito;

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

        ValidationTask validateTask = (ValidationTask) project.getTasksByName("versionCheck", false).iterator().next();

        ValidationReportRenderer mockRenderer = mock(ValidationReportRenderer.class);
        validateTask.setRenderer(mockRenderer);

        validateTask.execute();

        verify(mockRenderer).startProject(project);
        verify(mockRenderer).completeProject(project);

        Configuration configuration = project.getConfigurations().findByName("testCompile");

        verify(mockRenderer).startConfiguration(configuration);
        verify(mockRenderer).completeConfiguration(configuration);

        verify(mockRenderer).startViolationGroup(any());
        verify(mockRenderer).completeViolationGroup();

        verify(mockRenderer, times(4)).reportViolation(any(), anyVararg());

    }
}
