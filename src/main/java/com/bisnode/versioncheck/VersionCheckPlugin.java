
package com.bisnode.versioncheck;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.util.GradleVersion;

/**
 * VersionCheck plugin for Gradle.
 */
public class VersionCheckPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        GradleVersion version = GradleVersion.current();
        if (version.compareTo(GradleVersion.version("3.3")) < 0) {
            throw new IllegalStateException("Requires at least Gradle version 3.3");
        }

        project.getExtensions().create("versionCheck", VersionCheckExtension.class, project);
        project.task(createCheckTask(), "versionCheck");
        project.task(createReportTask(), "versionCheckReport");
    }

    private Map<String, ?> createCheckTask() {
        Map<String, Object> args = new HashMap<>();
        args.put(Task.TASK_TYPE, VersionCheckTask.class);
        args.put(Task.TASK_GROUP, "VersionCheck");
        args.put(Task.TASK_DESCRIPTION, "Applies the configured version checks to validate the dependency sets");
        return args;
    }

    private Map<String, ?> createReportTask() {
        Map<String, Object> args = new HashMap<>();
        args.put(Task.TASK_TYPE, VersionCheckReportTask.class);
        args.put(Task.TASK_GROUP, "VersionCheck");
        args.put(Task.TASK_DESCRIPTION, "Creates a report of the version checks for all configurations");
        return args;
    }
}

