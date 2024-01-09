package com.mll.automation.dashboard.reporting.convertor;

import com.mll.automation.dashboard.reporting.common.model.Bug;
import com.mll.automation.dashboard.reporting.dto.BugReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BugReportConvertor {
    private final DateConvertor dateConvertor;
    public List<Bug> convertToBugEntity(List<BugReportDTO> bugReportDTOList){
       List<Bug> bugList = bugReportDTOList.stream().map(dto->Bug.builder()
                .ticket(dto.getTicketId())
                .title(dto.getTitle())
                .qaOwner(dto.getQaOwner())
                .parentTicket(dto.getParentTicketId())
                .fromDate(dateConvertor.convert(dto.getFromDate()))
                .toDate(dateConvertor.convert(dto.getToDate()))
                .build()).collect(Collectors.toList());
        return bugList;
    }
    public BugReportDTO convertToBugReportDTOFromBugEntity(Bug source){
        if(null==source){
            return null;
        }
        return BugReportDTO.builder().parentTicketId(source.getParentTicket())
                .ticketId(source.getTicket())
                .title(source.getTitle())
                .qaOwner(source.getQaOwner()).build();
    }
}
