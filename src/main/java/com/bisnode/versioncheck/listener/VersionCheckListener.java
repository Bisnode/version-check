package com.bisnode.versioncheck.listener;

import org.gradle.api.artifacts.Configuration;

public interface VersionCheckListener {

    default void startConfiguration(Configuration configuration) {}
    default void completeConfiguration(Configuration configuration) {}

    default void startViolationGroup(String group) {}
    default void reportViolation(String formatText, Object... parameters) {}
    default void completeViolationGroup() {}
}
