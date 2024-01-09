package com.mll.automation.dashboard.reporting.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    void processAndSaveFile(MultipartFile file, Long fromDate, Long toDate) throws IOException;
    void generateTemplate(HttpServletResponse response) throws IOException;
    void exportReportByDateRange(Long fromDateTimeStamp, Long toDateTimeStamp, HttpServletResponse response) throws IOException;
}
