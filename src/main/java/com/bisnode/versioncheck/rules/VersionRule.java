package com.bisnode.versioncheck.rules;

import java.util.List;

import org.gradle.api.artifacts.ModuleVersionIdentifier;

import com.bisnode.versioncheck.listener.VersionCheckListener;

public interface VersionRule {

    boolean apply(List<ModuleVersionIdentifier> deps, VersionCheckListener renderer);

}
