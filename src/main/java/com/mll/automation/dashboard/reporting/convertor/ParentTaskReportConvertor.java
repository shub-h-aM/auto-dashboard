package com.mll.automation.dashboard.reporting.convertor;

import com.mll.automation.dashboard.reporting.common.model.ParentTask;
import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParentTaskReportConvertor {
    private final DateConvertor dateConvertor;
    public List<ParentTask> convertToParentTaskEntity(List<ParentTaskReportDTO> parentTaskReportDTOList){
        List<ParentTask> parentTaskList = parentTaskReportDTOList.stream().map(dto->ParentTask.builder()
                .ticketId(dto.getParentTicketId())
                .title(dto.getParentTicketTitle())
                .qaOwner(dto.getQaOwner())
                .automationTicket(dto.getAutomationTicket())
                .fromDate(dateConvertor.convert(dto.getFromDate()))
                .toDate(dateConvertor.convert(dto.getToDate()))
                .noOfEnhancement(dto.getNoOfEnhancement())
                .noOfRejection(dto.getNoOfRejection())
                .qaSuggestion(dto.getQaSuggestion())
                .status(dto.getStatus())
                .automationSanitySuiteUsed(dto.getAutomationSanitySuiteUsed())
                .noOfBlocker(dto.getNoOfBlocker()).build()
        ).collect(Collectors.toList());
        return parentTaskList;
    }
    public ParentTaskReportDTO convertFromParentTaskEntityToParentTaskReport(ParentTask source){
        if(null==source){
            return null;
        }
        return ParentTaskReportDTO.builder().parentTicketId(source.getTicketId())
                .parentTicketTitle(source.getTitle())
                .status(source.getStatus())
                .qaOwner(source.getQaOwner())
                .noOfEnhancement(source.getNoOfEnhancement())
                .noOfBlocker(source.getNoOfBlocker())
                .automationTicket(source.getAutomationTicket())
                .qaSuggestion(source.getQaSuggestion())
                .automationSanitySuiteUsed(source.getAutomationSanitySuiteUsed())
                .noOfRejection(source.getNoOfRejection()).build();
    }
}
