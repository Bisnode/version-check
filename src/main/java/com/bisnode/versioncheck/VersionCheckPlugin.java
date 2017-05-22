
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

        // create extension
        project.getExtensions().create("versioncheck", VersionCheckExtension.class, project);

        Map<String, Object> args = new HashMap<>();
        args.put(Task.TASK_TYPE, ValidationTask.class);
        Task jsonTask = project.task(args, "versionCheck");

        jsonTask.setDescription("Generate a pom.json file with the project\"s dependencies");
        jsonTask.setGroup("VersionCheck");
        jsonTask.doLast((task) -> {
            VersionCheckExtension ext = (VersionCheckExtension) project.getExtensions().getByName("versioncheck");
        });
    }
}

