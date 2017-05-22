package com.bisnode.versioncheck.rules;

import java.util.List;

import org.gradle.api.artifacts.ModuleVersionIdentifier;

public interface VersionRule {

    void apply(List<ModuleVersionIdentifier> allDeps);

}
