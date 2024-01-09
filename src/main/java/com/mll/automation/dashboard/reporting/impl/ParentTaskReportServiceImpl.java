package com.mll.automation.dashboard.reporting.impl;

import com.mll.automation.dashboard.reporting.common.model.ParentTask;
import com.mll.automation.dashboard.reporting.common.repository.ParentTaskReportRepository;
import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.convertor.DateConvertor;
import com.mll.automation.dashboard.reporting.convertor.ParentTaskReportConvertor;
import com.mll.automation.dashboard.reporting.service.ParentTaskReportService;
import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParentTaskReportServiceImpl implements ParentTaskReportService {
    private final ParentTaskReportConvertor parentTaskReportConvertor;
    private final ParentTaskReportRepository parentTaskReportRepository;
    private final DateConvertor dateConvertor;
    @Override
    public void processReport(List<ReportDTO> reportDTOList) {
        //TODO if sheet has empty or parent ticket id, throw error msg to user
        //TODO Need to handle the parent ticket title length for the UI it should not be max
        List<ParentTaskReportDTO> parentTaskReportDTOList =reportDTOList.stream()
                .filter(dto -> dto.getParentTaskTitle() != null && dto.getParentTaskID() !=null)
                .map(dto -> ParentTaskReportDTO.builder()
                        .parentTicketId(dto.getParentTaskID())
                        .parentTicketTitle(dto.getParentTaskTitle())
                        .qaOwner(dto.getQaOwner())
                        .status(dto.getTicketStatus())
                        .automationTicket(dto.getAutomationUsedTicketId())
                        .noOfBlocker(dto.getNoOfBlockers())
                        .noOfEnhancement(dto.getNoOfEnhancement())
                        .qaSuggestion(dto.getQaSuggestion())
                        .automationSanitySuiteUsed(dto.getAutomationSanitySuiteUsed())
                        .fromDate(dto.getFromDate())
                        .toDate(dto.getToDate())
                        .build()).collect(Collectors.toList());
        List<ParentTask> parentTaskList = parentTaskReportConvertor.convertToParentTaskEntity(parentTaskReportDTOList);
        parentTaskReportRepository.saveAll(parentTaskList);

        log.info("ParentReportDTO List:");
        parentTaskReportDTOList.forEach(dto->
                log.info("ParentTicketId: {}, Title: {}, QaOwner: {}, Status: {}", dto.getParentTicketId(),
                        dto.getParentTicketTitle(),dto.getQaOwner(),dto.getStatus()));
        log.info("SuccessFully Handle Parent Task Report Data");
    }

    @Override
    public Page<ParentTaskReportDTO> getPaginatedParentReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp) {
        Date fromDate = dateConvertor.convert(fromDateTimeStamp);
        Date toDate = dateConvertor.convert(toDateTimeStamp);
        Pageable pageable = PageRequest.of(pageNo-ReportConstant.PAGE_OFFSET, ReportConstant.NO_OF_ROWS_PER_PAGE);
        Page<ParentTask> parentTaskPage = parentTaskReportRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromDate,toDate,pageable);
        return parentTaskPage.map(parentTaskReportConvertor::convertFromParentTaskEntityToParentTaskReport);
    }

    @Override
    public List<Object[]> cardsAttributes(Long fromDateTimeStamp, Long toDateTimeStamp) {
        Date fromDate = dateConvertor.convert(fromDateTimeStamp);
        Date toDate = dateConvertor.convert(toDateTimeStamp);
        List<Object[]> fieldList = parentTaskReportRepository.getFieldsByDateRange(fromDate, toDate);
        log.info("Fetched fields for cards");
        return fieldList;
    }

    @Override
    public List<ParentTaskReportDTO> getReportByDateRange(Long fromDateTimeStamps, Long toDateTimeStamps) {
        List<ParentTask> parentTaskList = parentTaskReportRepository
                .findByFromDateGreaterThanEqualAndToDateLessThanEqual(dateConvertor.convert(fromDateTimeStamps),
                        dateConvertor.convert(toDateTimeStamps));
        return parentTaskList.stream().map(parentTaskReportConvertor::convertFromParentTaskEntityToParentTaskReport)
                .collect(Collectors.toList());
    }
}
