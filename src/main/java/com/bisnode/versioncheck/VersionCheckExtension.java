package com.bisnode.versioncheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gradle.api.Project;

import com.bisnode.versioncheck.rules.SameVersionGroupRule;
import com.bisnode.versioncheck.rules.VersionRule;

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
     * Configuration to include when calculating the dependencies.
     */
    private final Set<String> configurations;

    private final Set<VersionRule> versionRules = new HashSet<VersionRule>();

    /**
     * If dependencies from sub-projects should be included.
     */
    private boolean includeSubProjects = false;

    public VersionCheckExtension(Project project) {
        this.configurations = new HashSet<>();
        this.configurations.add("testCompile");
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
    public void configurations(String... configs) {
        configurations.clear();
        configurations.addAll(Arrays.asList(configs));
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
        if (configurations.isEmpty()) {
            return true;
        }
        return configurations.contains(name);
    }

    public Set<VersionRule> getVersionRules() {
        return versionRules;
    }

    public void setSameVersionGroups(List<String> groups) {
        for (String group : groups) {
            versionRules.add(new SameVersionGroupRule(group));
        }
    }

}
