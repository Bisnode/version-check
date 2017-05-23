version-check
========================

Plugin for [Gradle](http://www.gradle.org/) to check update your project dependencies ...

It works quite similar to the [Enforcer plugin for Maven](http://maven.apache.org/enforcer/maven-enforcer-plugin/).

Usage
-----

The simplest way to apply the plugin to your Gradle build is to use the plugin mechanism:

```groovy
plugins {
    id "com.bisnode.versioncheck" version "1.0.0"
}
```


### Gradle tasks

The plugin comes with one main Gradle tasks:

***versionCheck*** - Run the version checks against the project

#### Rules

##### Check for identical versions:

Specify the set of modules that should have the same version

```groovy
versionCheck {
    sameVersionGroups = [ "org.springframework.boot" ]
}
```


#### Which dependencies?

There are two main modes, you can use only the **declared** dependencies or additionally the **transitive** dependencies:

* **declared** - only first level dependencies are included (default)
* **transitive** - the declared and all transitive dependencies

Configuration example:
```groovy

versionCheck {
    dependencies = 'transitive'
}
```

#### Configurations

To further customize which dependencies are analyzed, you can specify configurations, for example to include the dependencies that are only needed for tests with the Java plugin:
```groovy

versionCheck {
    configurations 'testCompile'
}
```

**Tip:** If there are dependencies showing up you have no idea where they are coming from, use `gradle dependencies` to get an overview of all configurations and the dependencies contained in them. Use it to identifiy the configurations that you want to include.


##### Multi-project builds

If you have a multi-build project that you want to handle as one single VersionEye project, you should apply the plugin only to the root project and configure the plugin to include dependencies from sub-projects as well:

```groovy
versionCheck {
  includeSubProjects = true
}
```


License
-----

to be decided
