
package com.bisnode.versioncheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.tasks.TaskAction;

import com.bisnode.versioncheck.deps.DependencyUtils;
import com.bisnode.versioncheck.listener.VersionCheckErrorListener;
import com.bisnode.versioncheck.listener.VersionCheckListener;
import com.bisnode.versioncheck.rules.VersionRule;

/**
 * Validates the project dependencies against the defined ruleset.
 */
public class VersionCheckTask extends DefaultTask {

    private final VersionCheckExtension extension = (VersionCheckExtension) getProject().getExtensions().getByName("versionCheck");

    private final VersionCheckListener listener = new VersionCheckErrorListener();

    @TaskAction
    public void run() {
        // determine projects for which to include dependencies
        Set<Project> depProjects = determineProjects();

        for (Project project : depProjects) {
            for (Configuration configuration : getAcceptedConfigurations(project)) {
                listener.startConfiguration(configuration);
                applyChecks(configuration);
                listener.completeConfiguration(configuration);
            }
        }
    }

    private Set<Project> determineProjects() {
        Project project = getProject();
        if (extension.isIncludeSubProjects()) {
            // project and sub-projects
            return project.getAllprojects();
        } else {
            // only the main project
            return Collections.singleton(project);
        }
    }

    private void applyChecks(Configuration config) {
        List<ModuleVersionIdentifier> dependencies = DependencyUtils.getDependencies(getProject(), config.getResolvedConfiguration());

        for (VersionRule rule : extension.getVersionRules()) {
            if (!rule.apply(dependencies, listener)) {
                throw new GradleException("Rule violation detected");
            }
        }
    }

    private List<Configuration> getAcceptedConfigurations(Project depProject) {
        ConfigurationContainer configs = depProject.getConfigurations();
        List<Configuration> results = new ArrayList<>();
        for (String name : configs.getNames()) {
            // check the build settings if configuration should be included
            if (extension.acceptConfiguration(name)) {
                Configuration cfg = configs.getByName(name);
                // check if the config is even resolvable for us (since gradle 3.3)
                if (cfg.isCanBeResolved()) {
                    results.add(cfg);
                }
            }
        }
        return results;
    }
}
