package com.mll.automation.dashboard.reporting.impl;

import com.mll.automation.dashboard.reporting.common.model.SubTask;
import com.mll.automation.dashboard.reporting.common.repository.SubTaskReportRepository;
import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.convertor.DateConvertor;
import com.mll.automation.dashboard.reporting.convertor.SubTaskReportConvertor;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import com.mll.automation.dashboard.reporting.dto.SubTaskReportDTO;
import com.mll.automation.dashboard.reporting.service.SubTaskReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubTaskReportServiceImpl implements SubTaskReportService {
    private final SubTaskReportConvertor subTaskReportConvertor;
    private final SubTaskReportRepository subTaskReportRepository;
    private final DateConvertor dateConvertor;
    private static  String lastNonNullParentTicketId;
    @Override
    public void processReport(List<ReportDTO> reportDTOList) {

        //TODO if sheet has empty or parent ticket id, throw error msg to user
        List<SubTaskReportDTO> subTaskReportDTOList = reportDTOList.stream()
                .peek(dto -> {
                    String parentTicketId = dto.getParentTaskID();

                    if (parentTicketId == null) {
                        dto.setParentTaskID(lastNonNullParentTicketId);
                    } else {
                        lastNonNullParentTicketId = parentTicketId;
                    }
                })
                .filter(dto -> dto.getSubTaskTitle() != null)
                .map(dto -> SubTaskReportDTO.builder()
                        .parentTicketId(dto.getParentTaskID())
                        .ticketId(dto.getSubTaskId())
                        .title(dto.getSubTaskTitle())
                        .fromDate(dto.getFromDate())
                        .toDate(dto.getToDate())
                        .qaOwner(dto.getQaOwner())
                        .build())
                .collect(Collectors.toList());

        log.info("SubTaskReportDTO List:");
        subTaskReportDTOList.forEach(dto->
                log.info("ParentTicketId: {}, SubTaskTicketId: {}, Title: {}, QaOwner: {}", dto.getParentTicketId(),
                        dto.getTicketId(), dto.getTitle(), dto.getQaOwner()));
        List<SubTask> subTaskList = subTaskReportConvertor.convertToSubTaskEntity(subTaskReportDTOList);
        subTaskReportRepository.saveAll(subTaskList);
        log.info("SuccessFully Handle Sub Task Report Data");
    }

    @Override
    public Page<SubTaskReportDTO> getPaginatedSubReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp) {
        Date fromDate = dateConvertor.convert(fromDateTimeStamp);
        Date toDate = dateConvertor.convert(toDateTimeStamp);
        Pageable pageable = PageRequest.of(pageNo-ReportConstant.PAGE_OFFSET, ReportConstant.NO_OF_ROWS_PER_PAGE);
        Page<SubTask> subTaskPage = subTaskReportRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromDate,toDate,pageable);
        return subTaskPage.map(subTaskReportConvertor::convertFromSubTaskEntityToSubTaskReport);
    }

    @Override
    public List<SubTaskReportDTO> getReportByDateRange(Long fromDateTimeStamps, Long toDateTimeStamps) {
        List<SubTask> subTaskList = subTaskReportRepository
                .findByFromDateGreaterThanEqualAndToDateLessThanEqual(dateConvertor.convert(fromDateTimeStamps), dateConvertor.convert(toDateTimeStamps));
        return subTaskList.stream().map(subTaskReportConvertor::convertFromSubTaskEntityToSubTaskReport).collect(Collectors.toList());
    }

    @Override
    public Map mapSubTaskReportDTOToParentTicket(List<SubTaskReportDTO> subTaskReportDTOList) {
        Map<String, List<SubTaskReportDTO>> parentTicketIdSToSubTaskReportDTOMap = subTaskReportDTOList.stream().collect(Collectors
                .groupingBy(SubTaskReportDTO::getParentTicketId, Collectors.toList()));
        log.info("Successfully mapped the Sub task to its parent and size {}",parentTicketIdSToSubTaskReportDTOMap.size());
        return parentTicketIdSToSubTaskReportDTOMap;
    }
}
