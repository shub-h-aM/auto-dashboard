package com.mll.automation.dashboard.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTaskReportDTO {
    private String ticketId;
    private String title;
    private String parentTicketId;
    private String qaOwner;
    private Long fromDate;
    private Long toDate;
}
