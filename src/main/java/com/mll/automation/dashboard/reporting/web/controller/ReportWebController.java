package com.mll.automation.dashboard.reporting.web.controller;

import com.mll.automation.dashboard.reporting.dto.BugReportDTO;
import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import com.mll.automation.dashboard.reporting.dto.SubTaskReportDTO;
import com.mll.automation.dashboard.reporting.service.BugReportService;
import com.mll.automation.dashboard.reporting.service.ParentTaskReportService;
import com.mll.automation.dashboard.reporting.service.SubTaskReportService;
import com.mll.automation.dashboard.reporting.web.util.ReportModelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;


@Controller
@RequestMapping("report")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReportWebController {
    private final ParentTaskReportService parentTaskReportService;
    private final SubTaskReportService subTaskReportService;
    private final BugReportService bugReportService;
    @RequestMapping("/parent-report/{page}/{fromDAte}/{toDate}")
    public String getParentReportPaginatedPage(@PathVariable("page") Integer page,
                                               @PathVariable("fromDAte") Long fromDate,
                                               @PathVariable("toDate") Long toDate,
                                               Model model){
        log.info("Start parent report execution with date filter");
        Page<ParentTaskReportDTO> parentTaskDTOPage = parentTaskReportService.getPaginatedParentReportByFromAndToDate(page,fromDate,toDate);
        List<Object[]> cardsAttribute = parentTaskReportService.cardsAttributes(fromDate, toDate);
        model.addAttribute("title","Parent Task Report");
        model.addAttribute("currentPage",page);
        log.info("Fetched page size: {}",parentTaskDTOPage.getTotalPages());
        model.addAttribute("totalPages",parentTaskDTOPage.getTotalPages());
        model.addAttribute("parentTaskDTOPage",parentTaskDTOPage);
        model.addAttribute("released",ReportModelUtil.getSumOfTaskReleasedForParentModel(cardsAttribute));
        model.addAttribute("blocker",ReportModelUtil.getSumOfBlockerForParentModel(cardsAttribute));
        model.addAttribute("rejection",ReportModelUtil.getSumOfRejectionForParentModel(cardsAttribute));
        model.addAttribute("suggestion",ReportModelUtil.getSumOfQASuggestionForParentModel(cardsAttribute));
        model.addAttribute("enhancement",ReportModelUtil.getSumOfEnhancementForParentModel(cardsAttribute));
        return "reporting/parent-task-report";
    }
    @RequestMapping("/sub-report/{page}/{fromDAte}/{toDate}")
    public String getSubReportPaginatedPage(@PathVariable("page") Integer page,
                                               @PathVariable("fromDAte") Long fromDate,
                                               @PathVariable("toDate") Long toDate,
                                               Model model){
        log.info("Start parent report execution with date filter");
        Page<SubTaskReportDTO> subTaskDTOPage = subTaskReportService.getPaginatedSubReportByFromAndToDate(page,fromDate,toDate);
        model.addAttribute("title","Sub Task Report");
        model.addAttribute("currentSubPage",page);
        log.info("Fetched page size: {}",subTaskDTOPage.getTotalPages());
        log.info("Total Keys {}",subTaskDTOPage.getTotalElements());
        model.addAttribute("totalPages",subTaskDTOPage.getTotalPages());
        model.addAttribute("subTaskDTOPage",subTaskDTOPage);
        return "reporting/sub-task-report";
    }
    @RequestMapping("/bug-report/{page}/{fromDAte}/{toDate}")
    public String getBugReportPaginatedPage(@PathVariable("page") Integer page,
                                            @PathVariable("fromDAte") Long fromDate,
                                            @PathVariable("toDate") Long toDate,
                                            Model model){
        log.info("Start parent report execution with date filter");
        Page<BugReportDTO> bugReportDTOPage = bugReportService.getPaginatedBugReportByFromAndToDate(page,fromDate,toDate);
        model.addAttribute("title","Bug Report");
        model.addAttribute("currentBugPage",page);
        log.info("Fetched page size: {}",bugReportDTOPage.getTotalPages());
        log.info("Total Keys {}",bugReportDTOPage.getTotalElements());
        model.addAttribute("totalPages",bugReportDTOPage.getTotalPages());
        model.addAttribute("bugReportDTOPage",bugReportDTOPage);
        return "reporting/bug-report";
    }
}