
package com.bisnode.versioncheck;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.diagnostics.internal.TextReportRenderer;
import org.gradle.internal.logging.text.StyledTextOutput.Style;

/**
 * Render the rule violation of a project report.
 */
public class ValidationReportRenderer extends TextReportRenderer {

    private boolean isEmpty;

    @Override
    protected String createHeader(Project project) {
        String header = super.createHeader(project);
        return "All validation results from " + header;
    }

    public void startConfiguration(Configuration configuration) {
        getBuilder().subheading("Configuration: " + configuration.getName());
        isEmpty = true;
    }

    public void completeConfiguration(Configuration configuration) {
        if (isEmpty) {
            getTextOutput().withStyle(Style.Info).println("No violations detected");
            getTextOutput().println();
        }
    }

    public void startViolationGroup(String group) {
        getTextOutput().append(" o ").withStyle(Style.Error).append(group);
        getTextOutput().println();
        isEmpty = false;
    }

    public void reportViolation(String formatText, Object... parameters) {
        getTextOutput().append(" |-- ").withStyle(Style.Identifier).format(formatText,  parameters);
        getTextOutput().println();
    }

    public void completeViolationGroup() {
        if (!isEmpty) {
            getTextOutput().println();
        }
    }

}
