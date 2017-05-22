package com.bisnode.versioncheck;

import static org.junit.Assert.assertTrue;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.gradle.testfixtures.ProjectBuilder;

import org.junit.Test;

public class VersionCheckPluginTest {

    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();

        project.getPluginManager().apply("com.bisnode.versioncheck");
        project.getRepositories().mavenCentral();

        project.getConfigurations().create("compile");
        project.getDependencies().add("compile", new DefaultExternalModuleDependency("org.springframework.boot", "spring-boot", "1.5.1.RELEASE"));
        project.getDependencies().add("compile", new DefaultExternalModuleDependency("org.springframework.boot", "spring-boot-starter-web", "1.4.1.RELEASE"));

        VersionCheckExtension extension = project.getExtensions().findByType(VersionCheckExtension.class);
        extension.setDependencies("transitive");

        ValidationTask validateTask = (ValidationTask) project.getTasksByName("versionCheck", false).iterator().next();
        validateTask.execute();
    }
}
