version-check
========================

Plugin for [Gradle](http://www.gradle.org/) to check update your project dependencies ...

It works quite similar to the [xxxx plugin for Maven](https://xxxx).

Usage
-----

The simplest way to apply the plugin to your Gradle build is to use the plugin mechanism:

```groovy
plugins {
    id "org.standardout.versioneye" version "1.4.0"
}
```


### Gradle tasks

The plugin comes with two main Gradle tasks that are relevant for you:

* ***taskA*** - Creates a project 
* ***taskB*** - Updates the dependencies 
 

#### Which dependencies?

There are two main modes, you can use only the **declared** dependencies or additionally the **transitive** dependencies:

* **declared** - only first level dependencies are included (default)
* **transitive** - the declared and all transitive dependencies

Configuration example:
```groovy
versioneye {
  dependencies = transitive
}
```

To further customize which dependencies are analyzed, you can exclude specific configurations, for example to exclude the dependencies that are only needed for tests with the Gradle Java plugin:
```groovy
versioneye {
  exclude 'testCompile', 'testRuntime'
}
```

Please note that if you exclude a configuration that is extended by another configuration that you did not exclude, this will have no effect (e.g. if you exclude *runtime* but don't exclude *testRuntime*).

**Tip:** If there are dependencies showing up you have no idea where they are coming from, use `gradle dependencies` to get an overview of all configurations and the dependencies contained in them. Use it to identifiy the configurations that you don't want to include.

Since version 1.3, the plugins that you use for your build script are also included in the dependencies reported to VersionEye. If you don't want that, you can disable this feature in the configuration:

```groovy
versioneye {
  includePlugins = false
}
```


##### Multi-project builds (since 1.4)

If you have a multi-build project that you want to handle as one single VersionEye project, you should apply the plugin only to the root project and configure the plugin to include dependencies from sub-projects as well:

```groovy
version-check {
  includeSubProjects = true
}
```



#### Dependency scopes

The dependency scope in VersionEye is used to organize dependencies in different groups, for instance compile time dependencies or test dependencies.

Your project dependencies in Gradle are organised in dependency configurations, for instance in most projects there is a `compile` configuration. The dependency scope is determined based on the information in which configurations it is present.
The default strategy tries to identify the primary configuration of a dependency and use that as a scope.
This works best for standard project setups, so if you feel that you can provide a more optimal grouping, you can provide your own implementation.

The `DEFAULT` strategy is configured if you do not override the setting. Another provided strategy is the `CONFIGURATIONS` strategy.
It uses the configuration associations as is. You can enable it like this:

```groovy
version-check {
  determineScopeStrategy = CONFIGURATIONS
}
```

License
-----

to be decided
