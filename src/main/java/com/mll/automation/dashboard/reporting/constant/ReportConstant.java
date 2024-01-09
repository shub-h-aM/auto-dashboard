package com.mll.automation.dashboard.reporting.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportConstant {
    public Integer NO_OF_ROWS_PER_PAGE = 7;
    public Integer PAGE_OFFSET = 1;
    public String RELEASED_TICKET_STATUS = "released";
    public Integer SHEET_INDEX_TO_READ_DATA = 0;
    public String TEMPLATE_NAME = "QA Monthly Report Template";
    public Integer INDEX_AUTOMATION_TICKET = 0;
    public Integer INDEX_NO_OF_BLOCKER = 1;
    public Integer INDEX_NO_OF_REJECTION = 2;
    public Integer INDEX_NO_OF_ENHANCEMENT = 3;
    public Integer INDEX_QA_SUGGESTION = 4;
    public Integer INDEX_STATUS = 5;
    public Integer UPLOAD_DOWNLOAD_REPORT_HEADER = 0;
    public Integer CARDS_ROW_NO = 0;
    public Integer HEADER_ROW_OF_REPORT = 1;
    public Integer  TASK_RELEASED_CELL_NO = 0;
    public Integer BLOCKER_CELL_NO = 1;
    public Integer REJECTION_CELL_NO = 2;
    public Integer QA_SUGGESTION_CELL_NO = 3;
    public Integer ENHANCEMENT_CELL_NO = 4;
    public Integer AUTOMATION_SUITE_USED_CELL_NO = 5;
    public Integer AUTOMATION_TASK_CELL_NO = 6;
    public Integer BUG_REPORTED_CELL_NO = 7;
    public String AUTOMATION_SANITY_SUITE_USED_CARD_NAME = "Number of Automation Sanity Suite Used";
    public String TASK_RELEASED_CARD_NAME = "Number of  Task Released";
    public String BLOCKER_CARD_NAME = "Number of Blockers";
    public String QA_SUGGESTION_CARD_NAME = "Number of QA Suggestions";
    public String REJECTION_CARD_NAME = "Number of Build Rejections";
    public String ENHANCEMENT_CARD_NAME = "Number of Enhancements";
    public String AUTOMATION_TASK_CARD_NAME = "Number of Automation Task";
    public String BUG_REPORTED_CARD_NAME = "Number of Bugs Reported";

}
