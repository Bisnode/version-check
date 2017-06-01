
package com.bisnode.versioncheck.listener;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.diagnostics.internal.TextReportRenderer;
import org.gradle.internal.logging.text.StyledTextOutput.Style;

/**
 * Render the rule violation of a project report.
 */
public class VersionCheckReportRenderer extends TextReportRenderer implements VersionCheckListener {

    private boolean isEmpty;

    @Override
    protected String createHeader(Project project) {
        String header = super.createHeader(project);
        return "All validation results from " + header;
    }

    @Override
    public void startConfiguration(Configuration configuration) {
        getBuilder().subheading("Configuration: " + configuration.getName());
        isEmpty = true;
    }

    @Override
    public void completeConfiguration(Configuration configuration) {
        if (isEmpty) {
            getTextOutput().withStyle(Style.Info).println("No violations detected");
            getTextOutput().println();
        }
    }

    @Override
    public void startViolationGroup(String group) {
        CharSequence text = "Version group mismatch for '" + group + "'";
        getTextOutput().append(" o ").withStyle(Style.Error).append(text);
        getTextOutput().println();
        isEmpty = false;
    }

    @Override
    public void reportViolation(String formatText, Object... parameters) {
        getTextOutput().append(" |-- ").withStyle(Style.Identifier).format(formatText,  parameters);
        getTextOutput().println();
    }

    @Override
    public void completeViolationGroup() {
        if (!isEmpty) {
            getTextOutput().println();
        }
    }

}
