package com.mll.automation.dashboard.reporting.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportFormatDTO {
    private String parentTaskId;
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
}
