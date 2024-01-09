package com.mll.automation.dashboard.reporting.service;

import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import com.mll.automation.dashboard.reporting.dto.SubTaskReportDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface SubTaskReportService {
    void processReport(List<ReportDTO> reportDTOList);
    Page<SubTaskReportDTO> getPaginatedSubReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp);
    List<SubTaskReportDTO> getReportByDateRange(Long fromDateTimeStamps, Long toDateTimeStamps);
    Map mapSubTaskReportDTOToParentTicket(List<SubTaskReportDTO> subTaskReportDTOList);
}
