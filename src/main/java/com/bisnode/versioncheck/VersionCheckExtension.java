package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.gradle.api.Project;

public class VersionCheckExtension {

    // dependencies values
    public static final String transitive = "transitive";
    public static final String declared = "declared";

    /**
     * States which dependencies are analyzed - only the defined or all resolved dependencies.
     */
    private String dependencies = declared;

    private Project project;

    /**
     * Configuration to exclude when calculating the dependencies.
     */
    final Set<String> includeConfigurations = new HashSet<String>();

    /**
     * If dependencies from sub-projects should be included.
     */
    private boolean includeSubProjects = false;

    public VersionCheckExtension(Project project) {
        this.project = project;
      }

    public boolean isIncludeSubProjects() {
      return includeSubProjects;
    }

    public void setIncludeSubProjects(boolean includeSubProjects) {
      this.includeSubProjects = includeSubProjects;
    }

    /**
     * Specify configurations to exclude.
     * @param configs an array of configurations
     */
    public void include(String... configs) {
        includeConfigurations.addAll(Arrays.asList(configs));
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Test if the given configuration should be excluded.
     */
    boolean acceptConfiguration(String name) {
        if (includeConfigurations.isEmpty()) {
            return true;
        }
        return includeConfigurations.contains(name);
    }

}
