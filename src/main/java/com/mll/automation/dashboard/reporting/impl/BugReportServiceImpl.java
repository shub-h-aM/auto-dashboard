package com.mll.automation.dashboard.reporting.impl;

import com.mll.automation.dashboard.reporting.common.model.Bug;
import com.mll.automation.dashboard.reporting.common.repository.BugReportRepository;
import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.convertor.BugReportConvertor;
import com.mll.automation.dashboard.reporting.convertor.DateConvertor;
import com.mll.automation.dashboard.reporting.dto.BugReportDTO;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import com.mll.automation.dashboard.reporting.service.BugReportService;
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
public class BugReportServiceImpl implements BugReportService {
    private final BugReportRepository bugReportRepository;
    private final BugReportConvertor bugReportConvertor;
    private final DateConvertor dateConvertor;
    private static String lastNonNullParentTicketId;
    @Override
    public void processReport(List<ReportDTO> reportDTOList) {
        //TODO if sheet has empty or parent ticket id, throw error msg to user
        List<BugReportDTO> bugReportDTOList = reportDTOList.stream()
                .peek(dto -> {
                    String parentTicketId = dto.getParentTaskID();

                    if (parentTicketId == null) {
                        dto.setParentTaskID(lastNonNullParentTicketId);
                    } else {
                        lastNonNullParentTicketId = parentTicketId;
                    }
                })
                .filter(dto->dto.getBugTitle()!=null)
                .map(dto->BugReportDTO.builder()
                        .ticketId(dto.getBugId())
                        .title(dto.getBugId())
                        .toDate(dto.getToDate())
                        .fromDate(dto.getFromDate())
                        .qaOwner(dto.getQaOwner())
                        .parentTicketId(dto.getParentTaskID()).build()).collect(Collectors.toList());

        List<Bug> bugList = bugReportConvertor.convertToBugEntity(bugReportDTOList);
        bugReportRepository.saveAll(bugList);
        log.info("Bug DTO ");
        bugReportDTOList.forEach(dto ->
                log.info("ParentTicketId: {}, TicketId: {}, Title: {}, QaOwner: {}",
                dto.getParentTicketId(), dto.getTicketId(), dto.getTitle(), dto.getQaOwner()));
    }

    @Override
    public Page<BugReportDTO> getPaginatedBugReportByFromAndToDate(Integer pageNo, Long fromDateTimeStamp, Long toDateTimeStamp) {
        Date fromDate = dateConvertor.convert(fromDateTimeStamp);
        Date toDate = dateConvertor.convert(toDateTimeStamp);
        Pageable pageable = PageRequest.of(pageNo- ReportConstant.PAGE_OFFSET, ReportConstant.NO_OF_ROWS_PER_PAGE);
        Page<Bug> bugPage = bugReportRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromDate,toDate,pageable);
        return bugPage.map(bugReportConvertor::convertToBugReportDTOFromBugEntity);
    }

    @Override
    public List<BugReportDTO> getReportByDateRange(Long fromTimestamps, Long toDateTimeStamps) {
        List<Bug> bugList = bugReportRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(dateConvertor.convert(fromTimestamps),
                dateConvertor.convert(toDateTimeStamps));
        return bugList.stream().map(bugReportConvertor::convertToBugReportDTOFromBugEntity).collect(Collectors.toList());
    }

    @Override
    public Map mapBugReportDTOToParentTicket(List<BugReportDTO> bugReportDTOList) {
        Map<String,List<BugReportDTO>> parentTicketIdSToBugReportDTOMap = bugReportDTOList.stream()
                .collect(Collectors.groupingBy(BugReportDTO::getParentTicketId, Collectors.toList()));
        return parentTicketIdSToBugReportDTOMap;
    }
}
