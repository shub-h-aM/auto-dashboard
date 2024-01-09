package com.mll.automation.dashboard.reporting.convertor;

import com.mll.automation.dashboard.reporting.common.model.SubTask;
import com.mll.automation.dashboard.reporting.dto.SubTaskReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubTaskReportConvertor {
    private final DateConvertor dateConvertor;
    List<SubTask> subTaskList = new ArrayList<>();
    public List<SubTask> convertToSubTaskEntity(List<SubTaskReportDTO> subTaskReportDTOList){
        subTaskList = subTaskReportDTOList.stream().map(dto->SubTask.builder()
                .ticket(dto.getTicketId())
                .title(dto.getTitle())
                .qaOwner(dto.getQaOwner())
                .parentTicket(dto.getParentTicketId())
                .fromDate(dateConvertor.convert(dto.getFromDate()))
                .toDate(dateConvertor.convert(dto.getToDate()))
                .build()
        ).collect(Collectors.toList());
        return subTaskList;
    }
    public SubTaskReportDTO convertFromSubTaskEntityToSubTaskReport(SubTask source){
        if(null==source){
            return null;
        }
        return SubTaskReportDTO.builder().ticketId(source.getTicket())
                .parentTicketId(source.getParentTicket())
                .title(source.getTitle())
                .qaOwner(source.getQaOwner()).build();
    }
}
