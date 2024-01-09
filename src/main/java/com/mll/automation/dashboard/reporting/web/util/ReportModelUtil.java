package com.mll.automation.dashboard.reporting.web.util;

import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReportModelUtil {
    public static Double getSumOfBlockerForParentModel(List<Object[]> cardsAttributesList){
        return cardsAttributesList.stream()
                .mapToDouble(fields->Double.parseDouble((String) Optional.ofNullable(fields[ReportConstant.INDEX_NO_OF_BLOCKER])
                        .orElse("0")))
                .sum();
    }
    public static Double getSumOfRejectionForParentModel(List<Object[]> cardsAttributesList){
        return cardsAttributesList.stream()
                .mapToDouble(fields->Double.parseDouble((String) Optional.ofNullable(fields[ReportConstant.INDEX_NO_OF_REJECTION])
                .orElse("0"))).sum();
    }

    public static Long getSumOfTaskReleasedForParentModel(List<Object[]> cardsAttributesList){
        return cardsAttributesList.stream()
                .map(fields -> fields[ReportConstant.INDEX_STATUS])
                .filter(status -> status != null && ReportConstant.RELEASED_TICKET_STATUS.equalsIgnoreCase(status.toString()))
                .count();
    }

    public static Double getSumOfQASuggestionForParentModel(List<Object[]> cardsAttributesList){
        return cardsAttributesList.stream()
                .mapToDouble(fields->Double.parseDouble((String) Optional.ofNullable(fields[ReportConstant.INDEX_QA_SUGGESTION])
                        .orElse("0"))).sum();
    }

    public static Double getSumOfEnhancementForParentModel(List<Object[]> cardsAttributesList){
        return cardsAttributesList.stream()
                .mapToDouble(field->Double.parseDouble((String) Optional.ofNullable(field[ReportConstant.INDEX_NO_OF_ENHANCEMENT])
                        .orElse("0"))).sum();
    }
}
