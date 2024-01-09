package com.mll.automation.dashboard.reporting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExcelColumn {
    PARENT_TICKET_ID("Parent Ticket id"),
    PARENT_TICKET_TITLE("Parent Ticket title"),
    SUB_TASK_ASSOCIATED_WITH_PARENT("Sub Task id"),
    SUB_TASK_TITLE("Sub Task title"),
    BUG_TITLE("Bug title"),
    BUG_ID("Bug id"),
    AUTOMATION_USED_TICKET_ID("Automation ticket"),
    QA_OWNER_NAME("QA Owner"),
    AUTOMATION_SANITY_SUITE_USED("Automation sanity suite used"),
    TICKET_STATUS("Ticket status"),
    ADHOC_OR_REMARKS("Any tasks picked within date"),
    NUMBER_OF_BLOCKER("Number of blockers"),
    NUMBER_OF_ENHANCEMENT("Number of Enhancement"),
    NUMBER_OF_BUILD_REJECTED("Number of rejection"),
    QA_SUGGESTIONS("Number of suggestion");

    private final String displayString;
}
