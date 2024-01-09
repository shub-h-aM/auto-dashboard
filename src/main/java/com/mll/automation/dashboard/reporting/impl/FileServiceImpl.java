package com.mll.automation.dashboard.reporting.impl;

import com.mll.automation.dashboard.reporting.convertor.DateConvertor;
import com.mll.automation.dashboard.reporting.dto.BugReportDTO;
import com.mll.automation.dashboard.reporting.dto.ParentTaskReportDTO;
import com.mll.automation.dashboard.reporting.dto.SubTaskReportDTO;
import com.mll.automation.dashboard.reporting.exception.UserCustomExceptionWithStatusCode;
import com.mll.automation.dashboard.reporting.service.BugReportService;
import com.mll.automation.dashboard.reporting.service.ParentTaskReportService;
import com.mll.automation.dashboard.reporting.service.SubTaskReportService;
import com.mll.automation.dashboard.reporting.utility.FileUtility;
import com.mll.automation.dashboard.reporting.constant.ReportConstant;
import com.mll.automation.dashboard.reporting.dto.ReportDTO;
import com.mll.automation.dashboard.reporting.enums.ExcelColumn;
import com.mll.automation.dashboard.reporting.exception.UserCustomException;
import com.mll.automation.dashboard.reporting.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImpl implements FileService {
    private final ParentTaskReportService parentTaskReportService;
    private final SubTaskReportService subTaskReportService;
    private final BugReportService bugReportService;
    private final DateConvertor dateConvertor;
    @Override
    public void processAndSaveFile(MultipartFile file, Long fromDate, Long toDate) throws IOException {
        if(FileUtility.isEmpty(file) || null == file){
            throw new UserCustomExceptionWithStatusCode("File is empty", HttpStatus.BAD_REQUEST);
        }
        if(null == fromDate && null == toDate){
            throw new UserCustomExceptionWithStatusCode("Either from date or to date can not be null",HttpStatus.BAD_REQUEST);
        }
        Workbook workbook = FileUtility.getWorkBookFromInputStream(file.getInputStream());
        try {
            extractDataFromFile(workbook, fromDate, toDate);
        }
        finally {
            workbook.close();
            log.info("Workbook closed successfully");
        }
    }

    @Override
    public void generateTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        OutputStream outputStream = response.getOutputStream();
        try {
            Sheet  sheet = FileUtility.createSheet(workbook);
            final CellStyle headerRowStyle = FileUtility.getHeaderRowStyle(workbook);
            FileUtility.createTemplate(sheet,headerRowStyle);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"QA Monthly Report Template.xlsx\"");
            workbook.write(response.getOutputStream());
        }
        finally {
            workbook.close();
            outputStream.close();
        }
    }

    @Override
    public void exportReportByDateRange(Long fromDateTimeStamp, Long toDateTimeStamp,
                                        HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        OutputStream outputStream = response.getOutputStream();
        try {
            List<ParentTaskReportDTO> parentTaskReportDTOList = parentTaskReportService
                    .getReportByDateRange(fromDateTimeStamp,toDateTimeStamp);
            List<SubTaskReportDTO> subTaskReportDTOList = subTaskReportService
                    .getReportByDateRange(fromDateTimeStamp, toDateTimeStamp);
            List<BugReportDTO> bugReportDTOList = bugReportService
                    .getReportByDateRange(fromDateTimeStamp, toDateTimeStamp);
            Map<String, List<SubTaskReportDTO>> parentTicketIdSToSubTaskReportDTOMap = subTaskReportService.mapSubTaskReportDTOToParentTicket(subTaskReportDTOList);
            Map<String, List<BugReportDTO>> parentTicketIdSToBugReportDTOMap = bugReportService.mapBugReportDTOToParentTicket(bugReportDTOList);
            List<Object[]> cardsAttribute = parentTaskReportService.cardsAttributes(fromDateTimeStamp, toDateTimeStamp);
            FileUtility.generateReport(parentTaskReportDTOList,
                    parentTicketIdSToSubTaskReportDTOMap, parentTicketIdSToBugReportDTOMap, cardsAttribute, workbook,
            outputStream,response, dateConvertor.formatDateToDdMmYy(dateConvertor.convert(fromDateTimeStamp)),
                    dateConvertor.formatDateToDdMmYy(dateConvertor.convert(toDateTimeStamp)));
            try {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=\"QA Report.xlsx\"");
                workbook.write(outputStream);
            } catch (Exception exception) {
                throw new UserCustomException("Unable to generate file");
            }
        }
        finally {
            workbook.close();
            outputStream.close();
            log.info("Successfully closed the stream");
        }
    }

    public void extractDataFromFile(Workbook workbook, Long fromDate, Long toDate){
        Sheet sheet = workbook.getSheetAt(ReportConstant.SHEET_INDEX_TO_READ_DATA);
        log.info("Report Sheet Name: {}",sheet);
        Map<ExcelColumn, Integer> columnNameToIndexMap = new HashMap<>();
        columnNameToIndexMap = FileUtility.intilaiseColumnNameToIndexMap(columnNameToIndexMap);
        Row header = FileUtility.getHeader(sheet);
        columnNameToIndexMap = FileUtility.mapColumnWithPhysicalIndex(header,columnNameToIndexMap);
        List<ReportDTO> reportDTOList = FileUtility.getDataFromSheetAndMapToDTO(columnNameToIndexMap,sheet,fromDate,toDate);
        parentTaskReportService.processReport(reportDTOList);
        subTaskReportService.processReport(reportDTOList);
        bugReportService.processReport(reportDTOList);
    }
}
