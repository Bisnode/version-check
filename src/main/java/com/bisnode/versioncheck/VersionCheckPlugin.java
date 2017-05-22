
package com.bisnode.versioncheck;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.util.jar.*;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedConfiguration;
import org.gradle.api.artifacts.ResolvedArtifact;

/**
 * VersionEye plugin for Gradle.
 *
 * @author Simon Templer
 */
public class VersionCheckPlugin implements Plugin<Project> {


  public static final String PROP_PROJECT_ID = "versioneye.projectid";
  public static final String PROP_API_KEY = "versioneye.api_key";

  public static final String PROP_ORGANISATION = "versioneye.organisation";
  public static final String PROP_TEAM = "versioneye.team";

  private Project project;

  private File dependenciesFile;

  private File gradleProperties;

  @Override
  public void apply(Project project) {
    this.project = project;

    // create extension
    project.getExtensions().create("versioncheck", VersionCheckExtension.class, project);

    // task creating the POM representation of the dependencies
    Task jsonTask = project.task("versionCheck");

    jsonTask.setDescription("Generate a pom.json file with the project\"s dependencies");
    jsonTask.setGroup("VersionCheck");
    jsonTask.doLast((task) -> {
        VersionCheckExtension ext = (VersionCheckExtension) project.getExtensions().getByName("versioncheck");
        System.out.println("TASK: " + ext.getClass().getSimpleName());
    });
//    createTask.dependsOn(jsonTask)
  }

}
