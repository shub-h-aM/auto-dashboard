package com.mll.automation.dashboard.reporting.service;

import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import org.springframework.data.domain.Page;
import java.util.List;


public interface ParentTaskReportService {
    void processReport(List<ReportDTO> reportDTOList);
    Page<ParentTaskReportDTO> getPaginatedParentReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp);
    List<Object[]>  cardsAttributes(Long fromDateTimeStamp, Long toDateTimeStamp);
    List<ParentTaskReportDTO> getReportByDateRange(Long fromDateTimeStamps, Long toDateTimeStamps);
}
