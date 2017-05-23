/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Simon Templer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bisnode.versioncheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.ResolvedDependency;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.TaskAction;

import com.bisnode.versioncheck.rules.VersionRule;

/**
 * Validates the project dependencies against the defined ruleset.
 */
public class ValidationTask extends DefaultTask {

    private static final Logger logger = Logging.getLogger(ValidationTask.class);

    /**
     * Accepts only external dependencies.
     */
    private static class ExternalDependencySpec implements Spec<Dependency> {
        public boolean isSatisfiedBy(Dependency element) {
            return element instanceof ExternalDependency;
        }
    }

    private static final ExternalDependencySpec EXTERNAL_DEPENDENCY = new ExternalDependencySpec();

    private final VersionCheckExtension extension = (VersionCheckExtension) getProject().getExtensions().getByName("versionCheck");


    @TaskAction
    public void validate() {

        // determine projects for which to include dependencies
        Set<Project> depProjects = determineProjects();

        List<ModuleVersionIdentifier> allDeps = gatherDependencies(depProjects);

        applyChecks(allDeps);
    }

    private void applyChecks(List<ModuleVersionIdentifier> allDeps) {
        for (VersionRule rule : extension.getVersionRules()) {
            rule.apply(allDeps);
        }
    }

    private List<ModuleVersionIdentifier> gatherDependencies(Set<Project> depProjects) {
        List<ModuleVersionIdentifier> allDeps = new ArrayList<>();

        // project dependencies
        for (Project depProject : depProjects) {
            ConfigurationContainer configs = depProject.getConfigurations();
            for (String name : configs.getNames()) {
                // check if configuration should be included
                if (extension.acceptConfiguration(name)) {
                    Configuration cfg = configs.getByName(name);
                    if (cfg.isCanBeResolved()) {
                        ResolvedConfiguration config = cfg.getResolvedConfiguration();
                         allDeps.addAll(getDependencies(name, config));
                    }
                }
            }
        }

        // project plugins
//    if (project.versioneye.includePlugins) {
//      depProjects.each { Project depProject ->
//        depProject.buildscript.configurations.names.collect { String name ->
//          //XXX are there any build script configurations that should not be included?
//          ResolvedConfiguration config = depProject.buildscript.configurations.getByName(name).resolvedConfiguration
//          addDependenciesToMap(name, config, dependencyMap, 'plugin')
//        }
//      }
//    }
//

        return allDeps;
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

    private List<ModuleVersionIdentifier> getDependencies(String configName, ResolvedConfiguration resolvedConfig) {
        Set<?> deps;

        Project project = getProject();
        VersionCheckExtension versioncheckExt = (VersionCheckExtension) project.getExtensions().getByName("versionCheck");

        if (VersionCheckExtension.transitive.equals(versioncheckExt.getDependencies())) {
            // transitive dependencies: use all resolved artifacts

            // retrieve all external dependencies
            // XXX we are ignoring the dependency relations here - does it matter for VersionCheck?
            deps = resolvedConfig.getLenientConfiguration().getArtifacts(EXTERNAL_DEPENDENCY);
        } else if (VersionCheckExtension.declared.equals(versioncheckExt.getDependencies())) {
            // declared dependencies: only use first level dependencies
            deps = resolvedConfig.getLenientConfiguration().getFirstLevelModuleDependencies(EXTERNAL_DEPENDENCY);
        } else {
            logger.warn("Unknown dependency configuration - ignoring");
            deps = Collections.emptySet();
        }

        List<ModuleVersionIdentifier> dependencyList = new ArrayList<>();
        for (Object dependency : deps) {
            if (dependency instanceof ResolvedArtifact) {
                ResolvedArtifact artifact = (ResolvedArtifact) dependency;
                ModuleVersionIdentifier id = artifact.getModuleVersion().getId();
                dependencyList.add(id);
            } else if (dependency instanceof ResolvedDependency) {
                ResolvedDependency dep = (ResolvedDependency) dependency;
                ModuleVersionIdentifier id = dep.getModule().getId();
                dependencyList.add(id);
            }
        }
        return dependencyList;
    }

}
