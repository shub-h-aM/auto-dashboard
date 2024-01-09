package com.mll.automation.dashboard.reporting.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private String parentTaskID;
    private String parentTaskTitle;
    private String subTaskId;
    private String subTaskTitle;
    private String bugTitle;
    private String bugId;
    private String qaOwner;
    private String automationUsedTicketId;
    private String ticketStatus;
    private String noOfBlockers;
    private String noOfBuildRejects;
    private String noOfEnhancement;
    private String qaSuggestion;
    private String automationSanitySuiteUsed;
    private Long fromDate;
    private Long toDate;
}
