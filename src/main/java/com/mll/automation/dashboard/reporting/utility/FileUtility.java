package com.mll.automation.dashboard.reporting.utility;

import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.dto.*;
import com.mll.automation.dashboard.reporting.enums.ExcelColumn;
import com.mll.automation.dashboard.reporting.exception.UserCustomException;
import com.mll.automation.dashboard.reporting.exception.UserCustomExceptionWithStatusCode;
import com.mll.automation.dashboard.reporting.web.util.ReportModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class FileUtility {
    private static final String CAMEL_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String REPLACEMENT_PATTERN = "$1 $2";
    private static Map<String,Integer> qaSummaryReportHeaderIndexMap = new HashMap<>();
    public static Boolean isEmpty(MultipartFile file) {
        return Optional.ofNullable(file).map(MultipartFile::isEmpty).orElse(true);
    }
    public static Workbook getWorkBookFromInputStream(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        return workbook;
    }
    public static Map<ExcelColumn, Integer> intilaiseColumnNameToIndexMap(Map<ExcelColumn, Integer> columnMap) {
        for (ExcelColumn column : ExcelColumn.values()) {
            columnMap.put(column, -1);
        }
        return columnMap;
    }
    public static List getDataFromSheetAndMapToDTO(Map columnIndexMap, Sheet sheet, Long fromDate, Long toDate) {
        List<ReportDTO> reportDTOList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        log.info("File has total rows: {}", sheet.getLastRowNum());
        boolean isHeaderRow = true;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isHeaderRow) {
                isHeaderRow = false;
                continue;
            }
            log.info("Reading data from row : {}", row.getRowNum());
            if (validateNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_TITLE, columnIndexMap) && !(validateNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_ID, columnIndexMap))){
                throw new UserCustomExceptionWithStatusCode(ExcelColumn.PARENT_TICKET_ID.getDisplayString() + " cannot be empty or null"+" at row "+row.getRowNum() , HttpStatus.BAD_REQUEST);
            }
            if (validateNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_ID, columnIndexMap) && !(validateNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_TITLE, columnIndexMap))){
                throw new UserCustomExceptionWithStatusCode(ExcelColumn.PARENT_TICKET_TITLE.getDisplayString() + " cannot be empty or null"+" at row "+row.getRowNum() , HttpStatus.BAD_REQUEST);
            }

            ReportDTO reportDTO = ReportDTO.builder()
                    .parentTaskID(getNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_ID, columnIndexMap))
                    .parentTaskTitle(getNonEmptyStringValue(row, ExcelColumn.PARENT_TICKET_TITLE, columnIndexMap))
                    .subTaskId(getNonEmptyStringValue(row, ExcelColumn.SUB_TASK_ASSOCIATED_WITH_PARENT, columnIndexMap))
                    .subTaskTitle(getNonEmptyStringValue(row, ExcelColumn.SUB_TASK_TITLE, columnIndexMap))
                    .bugId(getNonEmptyStringValue(row, ExcelColumn.BUG_ID, columnIndexMap))
                    .bugTitle(getNonEmptyStringValue(row, ExcelColumn.BUG_TITLE, columnIndexMap))
                    .qaOwner(getNonEmptyStringValue(row, ExcelColumn.QA_OWNER_NAME, columnIndexMap))
                    .automationUsedTicketId(getNonEmptyStringValue(row, ExcelColumn.AUTOMATION_USED_TICKET_ID, columnIndexMap))
                    .noOfBlockers(getNonEmptyStringValue(row, ExcelColumn.NUMBER_OF_BLOCKER, columnIndexMap))
                    .noOfBuildRejects(getNonEmptyStringValue(row, ExcelColumn.NUMBER_OF_BUILD_REJECTED, columnIndexMap))
                    .noOfEnhancement(getNonEmptyStringValue(row, ExcelColumn.NUMBER_OF_ENHANCEMENT, columnIndexMap))
                    .qaSuggestion(getNonEmptyStringValue(row, ExcelColumn.QA_SUGGESTIONS, columnIndexMap))
                    .automationSanitySuiteUsed(getNonEmptyStringValue(row, ExcelColumn.AUTOMATION_SANITY_SUITE_USED, columnIndexMap))
                    .toDate(toDate)
                    .fromDate(fromDate)
                    .ticketStatus(getNonEmptyStringValue(row, ExcelColumn.TICKET_STATUS, columnIndexMap))
                    .build();

            reportDTOList.add(reportDTO);
        }
        log.info("Data fetched successfully from file ", reportDTOList);
        return reportDTOList;
    }
    private static Boolean validateNonEmptyStringValue(Row row, ExcelColumn excelColumn, Map<ExcelColumn, Integer> columnIndexMap) {
        String value = getNonEmptyStringValue(row, excelColumn, columnIndexMap);
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    private static String getNonEmptyStringValue(Row row, ExcelColumn column, Map columnIndexMap) {
        if (row != null) {
            log.info("column {} and column index map ",column,columnIndexMap);
            Cell cell = row.getCell((Integer) columnIndexMap.get(column));
            if (cell != null) {
                String cellValue = getStringValue(cell).trim();
                if (!cellValue.isEmpty()) {
                    return cellValue;
                }
            }
        }
        return null;
    }

    public static String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    public static Row getHeader(Sheet sheet) {
        return sheet.getRow(ReportConstant.UPLOAD_DOWNLOAD_REPORT_HEADER);
    }
    public static Map mapColumnWithPhysicalIndex(Row header, Map<ExcelColumn, Integer> columnIndexMap) {
        Iterator<Cell> headerCellIterator = header.cellIterator();
        while (headerCellIterator.hasNext()) {
            Cell cell = headerCellIterator.next();
            columnIndexMap = mapColumnWithPhysicalValueFromColumnName(cell, columnIndexMap);
        }
        log.info("Column and Index Map {}",columnIndexMap);
        return columnIndexMap;
    }
    public static Map mapColumnWithPhysicalValueFromColumnName(Cell cell, Map<ExcelColumn, Integer> colmnIndexMap) {
        for (ExcelColumn excelColumn : ExcelColumn.values()) {
            if (excelColumn.getDisplayString().equalsIgnoreCase(cell.getStringCellValue())) {
                colmnIndexMap.put(excelColumn, cell.getColumnIndex());
                break;
            }
        }
        return colmnIndexMap;
    }
    public static Sheet createSheet(Workbook workbook){
        Sheet sheet = workbook.createSheet(ReportConstant.TEMPLATE_NAME);
        return sheet;
    }
    public static CellStyle getHeaderRowStyle(Workbook wb) {
        final CellStyle headerRowStyle = wb.createCellStyle();
        headerRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerRowStyle;
    }
    public static Sheet createTemplate(Sheet sheet, CellStyle headerStyle){
        Row headerRow = sheet.createRow(ReportConstant.UPLOAD_DOWNLOAD_REPORT_HEADER);
        ExcelColumn[] columnNames = ExcelColumn.values();
        for (int headerCellIndex = 0; headerCellIndex < columnNames.length; headerCellIndex++) {
            Cell cell = headerRow.createCell(headerCellIndex);
            cell.setCellValue(columnNames[headerCellIndex].getDisplayString());
            cell.setCellStyle(headerStyle);
        }
        autoSizeRowColumns(sheet,headerRow.getRowNum());
        return sheet;
    }
    public static void generateReport(List<ParentTaskReportDTO> parentTaskReportDTOList,
                                Map<String, List<SubTaskReportDTO>> parentTicketIdSToSubTaskReportDTOMap,
                                Map<String, List<BugReportDTO>> parentTicketIdSToBugReportDTOMap, List<Object[]> cardsAttribute, Workbook workbook, OutputStream outputStream,
                                 HttpServletResponse response, String fromDate, String toDate){
        Sheet sheet = workbook.createSheet(generateSheetName(fromDate,toDate));
        handleCardsRow(sheet, cardsAttribute, workbook);
        handleSummaryReportHeader(sheet,workbook);
        writeQASummaryReport(parentTaskReportDTOList,parentTicketIdSToSubTaskReportDTOMap,parentTicketIdSToBugReportDTOMap,sheet);
    }
    public static String generateSheetName(String fromDate, String toDate){
        return fromDate.toString() + "_to_" + toDate.toString();
    }
    public static void handleCardsRow(Sheet sheet, List<Object[]> cardsAttribute, Workbook workbook){
        Row cardRow = sheet.createRow(ReportConstant.CARDS_ROW_NO);
        handleTaskReleasedCard(cardRow, cardsAttribute, workbook);
        handleBlockerCard(cardRow,cardsAttribute,workbook);
        handleRejectionCardCell(cardRow,cardsAttribute,workbook);
        handleQASuggestionCardCell(cardRow,cardsAttribute,workbook);
        handleEnhancementCardCell(cardRow,cardsAttribute,workbook);
        handleBugReportedCard(cardRow,cardsAttribute,workbook);
        handleAutomationSuiteUsedCard(cardRow,cardsAttribute,workbook);
        handleAutomationTaskCard(cardRow,cardsAttribute,workbook);
        autoSizeRowColumns(sheet,cardRow.getRowNum());
    }
    public static void handleTaskReleasedCard(Row cardRow, List<Object[]> cardsAttribute,
                                              Workbook workbook){
        Cell taskReleasedCell = cardRow.createCell(ReportConstant.TASK_RELEASED_CELL_NO);
        taskReleasedCell.setCellValue(ReportConstant.TASK_RELEASED_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfTaskReleasedForParentModel(cardsAttribute));
        CellStyle taskReleasedCardCellStyle = workbook.createCellStyle();
        taskReleasedCardCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCardCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        taskReleasedCell.setCellStyle(taskReleasedCardCellStyle);
    }
    public static void handleBlockerCard(Row cardRow, List<Object[]> cardsAttribute,
                                              Workbook workbook){
        Cell blockerCardCell = cardRow.createCell(ReportConstant.BLOCKER_CELL_NO);
        blockerCardCell.setCellValue(ReportConstant.BLOCKER_CARD_NAME+ ReportModelUtil
                .getSumOfBlockerForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        blockerCardCell.setCellStyle(taskReleasedCellStyle);
    }
    public static void handleRejectionCardCell(Row cardRow, List<Object[]> cardsAttribute,
                                               Workbook workbook){
        Cell buildRejectedCardCell = cardRow.createCell(ReportConstant.REJECTION_CELL_NO);
        buildRejectedCardCell.setCellValue(ReportConstant.REJECTION_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfRejectionForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        buildRejectedCardCell.setCellStyle(taskReleasedCellStyle);
    }
    public static void handleQASuggestionCardCell(Row cardRow, List<Object[]> cardsAttribute,
                                                  Workbook workbook){
        Cell qaSuggestionCardCell = cardRow.createCell(ReportConstant.QA_SUGGESTION_CELL_NO);
        qaSuggestionCardCell.setCellValue(ReportConstant.QA_SUGGESTION_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfQASuggestionForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        qaSuggestionCardCell.setCellStyle(taskReleasedCellStyle);
    }
    public static void handleEnhancementCardCell(Row cardRow, List<Object[]> cardsAttribute,
                                                 Workbook workbook){
        Cell blockerCardCell = cardRow.createCell(ReportConstant.ENHANCEMENT_CELL_NO);
        blockerCardCell.setCellValue(ReportConstant.ENHANCEMENT_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfEnhancementForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        blockerCardCell.setCellStyle(taskReleasedCellStyle);
    }
    public static void handleAutomationSuiteUsedCard(Row cardRow, List<Object[]> cardsAttribute,
                                                                  Workbook workbook){
        Cell taskReleasedCell = cardRow.createCell(ReportConstant.AUTOMATION_SUITE_USED_CELL_NO);
        taskReleasedCell.setCellValue(ReportConstant.AUTOMATION_SANITY_SUITE_USED_CARD_NAME +" : "+ ReportModelUtil
                .getSumOfTaskReleasedForParentModel(cardsAttribute));
        CellStyle taskReleasedCardCellStyle = workbook.createCellStyle();
        taskReleasedCardCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCardCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        taskReleasedCell.setCellStyle(taskReleasedCardCellStyle);
    }
    public static void handleAutomationTaskCard(Row cardRow, List<Object[]> cardsAttribute,
                                         Workbook workbook){
        Cell blockerCardCell = cardRow.createCell(ReportConstant.AUTOMATION_TASK_CELL_NO);
        blockerCardCell.setCellValue(ReportConstant.AUTOMATION_TASK_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfBlockerForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        blockerCardCell.setCellStyle(taskReleasedCellStyle);
    }
    public static void handleBugReportedCard(Row cardRow, List<Object[]> cardsAttribute,
                                         Workbook workbook){
        Cell blockerCardCell = cardRow.createCell(ReportConstant.BUG_REPORTED_CELL_NO);
        blockerCardCell.setCellValue(ReportConstant.BUG_REPORTED_CARD_NAME+" : "+ ReportModelUtil
                .getSumOfBlockerForParentModel(cardsAttribute));
        CellStyle taskReleasedCellStyle = workbook.createCellStyle();
        taskReleasedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        taskReleasedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        blockerCardCell.setCellStyle(taskReleasedCellStyle);
    }
    private static void autoSizeRowColumns(Sheet sheet, Integer rowNumber) {
        Row row = sheet.getRow(rowNumber);

        if (row != null) {
            int lastCellNum = row.getLastCellNum();
            int[] maxColumnWidths = new int[lastCellNum];

            for (int cellNo = 0; cellNo < lastCellNum; cellNo++) {
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row currentRow = sheet.getRow(rowNum);
                    Cell currentCell = (currentRow != null) ? currentRow.getCell(cellNo) : null;

                    if (currentCell != null && currentCell.getCellType() == CellType.STRING) {
                        String cellValue = currentCell.getStringCellValue();
                        int cellLength = cellValue.length();
                        maxColumnWidths[cellNo] = Math.max(maxColumnWidths[cellNo], cellLength);
                    }
                }

                sheet.setColumnWidth(cellNo, (maxColumnWidths[cellNo] + 2) * 256); // Adding a margin
            }
        }
    }

    private static void handleSummaryReportHeader(Sheet sheet, Workbook workbook){
        Row qaSummaryReportHeader = sheet.createRow(ReportConstant.HEADER_ROW_OF_REPORT);
        Field [] fields = ReportFormatDTO.class.getDeclaredFields();
        for(int qaSummaryReportHeaderColumn = 0; qaSummaryReportHeaderColumn < fields.length;){
            if(qaSummaryReportHeader != null){
                Cell qaSummaryReportHeaderColumnCell = qaSummaryReportHeader
                        .createCell(qaSummaryReportHeaderColumn);
                qaSummaryReportHeaderColumnCell.setCellValue(formatDTOFiledName(fields[qaSummaryReportHeaderColumn].getName()));
                qaSummaryReportHeaderColumnCell.setCellStyle(getHeaderRowStyle(workbook));
                qaSummaryReportHeaderIndexMap.put(formatDTOFiledName(fields[qaSummaryReportHeaderColumn]
                        .getName()),qaSummaryReportHeaderColumn);

            }
            else {
                throw new UserCustomException("QA Summary Report's header is null");
            }
            qaSummaryReportHeaderColumn++;
        }
    }
    private static String formatDTOFiledName(String fieldName){
        String formattedName = fieldName.replaceAll(CAMEL_CASE_REGEX, REPLACEMENT_PATTERN);
        return formattedName
                .substring(0, 1)
                .toUpperCase(Locale.ROOT)
                .concat(formattedName.substring(1));
    }
    private static void writeQASummaryReport(List<ParentTaskReportDTO> parentTaskDTOList, Map<String, List<SubTaskReportDTO>> parentTicketIdSToSubTaskReportDTOMap,
                                             Map<String, List<BugReportDTO>> parentTicketIdSToBugReportDTOMap, Sheet sheet) {
        //TODO Refactor me
        int rowCreationIndex = ReportConstant.CARDS_ROW_NO+2;
        int subTaskColumnRowIndex = rowCreationIndex;
        int bugDetailsColumnRowIndex = rowCreationIndex;
        int maxRowNoForParentTicket=0;
        int prev=ReportConstant.CARDS_ROW_NO+2;

        log.info("Start writing value to row ...");
        for (ParentTaskReportDTO parentTaskDTO : parentTaskDTOList) {

            List<SubTaskReportDTO> subTaskDTOList = parentTicketIdSToSubTaskReportDTOMap.getOrDefault(parentTaskDTO.getParentTicketId(), null);
            List<BugReportDTO> bugDTOList = parentTicketIdSToBugReportDTOMap.getOrDefault(parentTaskDTO.getParentTicketId(), null);

            int maxOfSubTaskAndBug = Math.max(subTaskDTOList.size(),bugDTOList.size());
            maxRowNoForParentTicket = maxRowNoForParentTicket+maxOfSubTaskAndBug+rowCreationIndex;
            log.info("Max. Of Sub Task and Bug is : {} and preview pointer: {} and Row creation pointer {} and Start Row {}",maxOfSubTaskAndBug,prev,maxRowNoForParentTicket,rowCreationIndex);
            while (prev<=maxRowNoForParentTicket){
                Row row = sheet.createRow(prev);
                log.info("Row created {}",row.getRowNum());
                prev++;
            }
            if(rowCreationIndex<maxRowNoForParentTicket){
                log.info("Map {} and dto for parent {}",qaSummaryReportHeaderIndexMap,parentTaskDTO.getParentTicketId());
                log.info("Map {} ",qaSummaryReportHeaderIndexMap.get("Parent Task Id"));
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Parent Task Id")).setCellValue(parentTaskDTO.getParentTicketId());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Parent Task Title")).setCellValue(parentTaskDTO.getParentTicketTitle());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Qa Owner")).setCellValue(parentTaskDTO.getQaOwner());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Ticket Status")).setCellValue(parentTaskDTO.getStatus());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Automation Used Ticket Id")).setCellValue(parentTaskDTO.getAutomationTicket());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("No Of Build Rejects")).setCellValue(parentTaskDTO.getNoOfRejection());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("No Of Enhancement")).setCellValue(parentTaskDTO.getNoOfEnhancement());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("Qa Suggestion")).setCellValue(parentTaskDTO.getQaSuggestion());
                sheet.getRow(rowCreationIndex).createCell(qaSummaryReportHeaderIndexMap.get("No Of Blockers")).setCellValue(parentTaskDTO.getNoOfBlocker());
            }
            if (!subTaskDTOList.isEmpty() && subTaskDTOList != null) {
                for (SubTaskReportDTO subTaskDTO : subTaskDTOList) {
                    if(subTaskColumnRowIndex<maxRowNoForParentTicket){
                        log.info("Sub Task Id {} ",subTaskDTO.getTicketId());
                        log.info("Creating Row for Sub Task");
                        Row row = sheet.getRow(subTaskColumnRowIndex);
                        log.info("Creating Cell for Sub Task for Row {}",row.getRowNum());
                        Cell cell = row.createCell(qaSummaryReportHeaderIndexMap.get("Sub Task Id"));
                        log.info("Setting its value");
                        cell.setCellValue(subTaskDTO.getTicketId());
                        sheet.getRow(subTaskColumnRowIndex++).createCell(qaSummaryReportHeaderIndexMap.get("Sub Task Title")).setCellValue(subTaskDTO.getTitle());
                    }
                }
            }
            if (!bugDTOList.isEmpty() && bugDTOList != null) {
                for (BugReportDTO bugDTO : bugDTOList) {
                    if(bugDetailsColumnRowIndex<maxRowNoForParentTicket){
                        sheet.getRow(bugDetailsColumnRowIndex).createCell(qaSummaryReportHeaderIndexMap.get("Bug Id")).setCellValue(bugDTO.getTicketId());
                        sheet.getRow(bugDetailsColumnRowIndex++).createCell(qaSummaryReportHeaderIndexMap.get("Bug Title")).setCellValue(bugDTO.getTitle());
                    }
                }
            }
            rowCreationIndex =prev;
            bugDetailsColumnRowIndex=prev;
            subTaskColumnRowIndex=prev;
            log.info("Parent row {} Sub {} Bug {}",rowCreationIndex,subTaskColumnRowIndex,bugDetailsColumnRowIndex);
        }
    }
}
