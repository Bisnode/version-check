package com.bisnode.versioncheck.deps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalDependency;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.ResolvedDependency;
import org.gradle.api.specs.Spec;

import com.bisnode.versioncheck.VersionCheckExtension;

public class DependencyUtils {

    /**
     * Accepts only external dependencies.
     */
    public static final Spec<Dependency> EXTERNAL_DEPENDENCY = new Spec<Dependency>() {
        public boolean isSatisfiedBy(Dependency element) {
            return element instanceof ExternalDependency;
        }
    };

    public static List<ModuleVersionIdentifier> getDependencies(Project project, ResolvedConfiguration resolvedConfig) {
        Set<?> deps;

        VersionCheckExtension versioncheckExt = (VersionCheckExtension) project.getExtensions().getByName("versionCheck");

        if (VersionCheckExtension.transitive.equals(versioncheckExt.getDependencies())) {
            // transitive dependencies: use all resolved artifacts

            // retrieve all external dependencies
            // XXX we are ignoring the dependency relations here - does it matter for VersionCheck?
            deps = resolvedConfig.getLenientConfiguration().getArtifacts(DependencyUtils.EXTERNAL_DEPENDENCY);
        } else if (VersionCheckExtension.declared.equals(versioncheckExt.getDependencies())) {
            // declared dependencies: only use first level dependencies
            deps = resolvedConfig.getLenientConfiguration().getFirstLevelModuleDependencies(DependencyUtils.EXTERNAL_DEPENDENCY);
        } else {
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
