
package com.bisnode.versioncheck;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * VersionCheck plugin for Gradle.
 */
public class VersionCheckPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getExtensions().create("versionCheck", VersionCheckExtension.class, project);

        Map<String, Object> args = new HashMap<>();
        args.put(Task.TASK_TYPE, ValidationTask.class);
        args.put(Task.TASK_GROUP, "VersionCheck");
        args.put(Task.TASK_DESCRIPTION, "Applies the configured version checks to validate the dependency sets");
        project.task(args, "versionCheck");
    }
}

