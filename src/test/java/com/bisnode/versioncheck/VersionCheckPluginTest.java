package com.bisnode.versioncheck;

import static org.junit.Assert.assertTrue;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.gradle.api.logging.StandardOutputListener;
import org.gradle.api.tasks.diagnostics.DependencyInsightReportTask;
import org.gradle.api.tasks.diagnostics.DependencyReportTask;
import org.gradle.testfixtures.ProjectBuilder;

import org.junit.Test;

import com.bisnode.versioncheck.rules.DuplicateRule;
import com.bisnode.versioncheck.rules.EqualsRule;

public class VersionCheckPluginTest {

    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();

        project.getPluginManager().apply("java");
        project.getPluginManager().apply("com.bisnode.versioncheck");
        project.getRepositories().maven(repo -> {
            repo.setUrl("https://artifactory.bisnode.net/artifactory/repo/");
        });

//        project.getConfigurations().create("compile");
//        project.getConfigurations().create("testCompile");
        project.getDependencies().add("compile", new DefaultExternalModuleDependency("com.bisnode", "spring-boot-microservice", "0.9.0"));
        project.getDependencies().add("testCompile", new DefaultExternalModuleDependency("org.springframework.boot", "spring-boot-starter-test", "1.4.1.RELEASE"));

        VersionCheckExtension extension = project.getExtensions().findByType(VersionCheckExtension.class);
        extension.setDependencies("transitive");

        extension.getVersionRules().add(new DuplicateRule());
        extension.getVersionRules().add(new EqualsRule("org.springframework.boot"));

        ValidationTask validateTask = (ValidationTask) project.getTasksByName("versionCheck", false).iterator().next();
        validateTask.execute();
    }
}
