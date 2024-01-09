package com.mll.automation.dashboard.reporting.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentTaskReportDTO {
    private String parentTicketId;
    private String parentTicketTitle;
    private String qaOwner;
    private String status;
    private String automationTicket;
    private String noOfBlocker;
    private String noOfRejection;
    private String noOfEnhancement;
    private String qaSuggestion;
    private String automationSanitySuiteUsed;
    private Long fromDate;
    private Long toDate;
}
