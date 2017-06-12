
package com.bisnode.versioncheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.tasks.diagnostics.AbstractReportTask;
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer;

import com.bisnode.versioncheck.deps.DependencyUtils;
import com.bisnode.versioncheck.listener.VersionCheckReportRenderer;
import com.bisnode.versioncheck.rules.VersionRule;

/**
 * Validates the project dependencies against the defined ruleset.
 */
public class VersionCheckReportTask extends AbstractReportTask {

    private final VersionCheckExtension extension = (VersionCheckExtension) getProject().getExtensions().getByName("versionCheck");

    private VersionCheckReportRenderer renderer = new VersionCheckReportRenderer();

    @Override
    protected ReportRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to use to build a report. If unset, ValidationReportRenderer will be used.
     */
    public void setRenderer(VersionCheckReportRenderer renderer) {
        this.renderer = renderer;
    }


    @Override
    protected void generate(Project project) throws IOException {
        for (Configuration configuration : getAcceptedConfigurations(project)) {
            renderer.startConfiguration(configuration);
            applyChecks(configuration);
            renderer.completeConfiguration(configuration);
        }
    }

    private void applyChecks(Configuration config) {
        List<ModuleVersionIdentifier> dependencies = DependencyUtils.getDependencies(getProject(), config.getResolvedConfiguration());

        for (VersionRule rule : extension.getVersionRules()) {
            rule.apply(dependencies, renderer);
        }
    }

    private List<Configuration> getAcceptedConfigurations(Project depProject) {
        ConfigurationContainer configs = depProject.getConfigurations();
        List<Configuration> results = new ArrayList<>();
        for (String name : configs.getNames()) {
            Configuration cfg = configs.getByName(name);
            // check if the config is even resolvable for us (since gradle 3.3)
            if (cfg.isCanBeResolved()) {
                results.add(cfg);
            }
        }
        return results;
    }

}
