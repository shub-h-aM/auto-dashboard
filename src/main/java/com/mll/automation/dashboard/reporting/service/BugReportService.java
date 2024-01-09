package com.mll.automation.dashboard.reporting.service;

import com.mll.automation.dashboard.reporting.dto.BugReportDTO;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BugReportService {
    void processReport(List<ReportDTO> reportDTOList);
    Page<BugReportDTO> getPaginatedBugReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp);
    List<BugReportDTO> getReportByDateRange(Long fromTimestamps, Long toDateTimeStamps);
    Map mapBugReportDTOToParentTicket(List<BugReportDTO> bugReportDTOList);
}
