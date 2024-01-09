package com.mll.automation.dashboard.reporting.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BugReportDTO {
    private String ticketId;
    private String title;
    private String parentTicketId;
    private String qaOwner;
    private Long fromDate;
    private Long toDate;
}
