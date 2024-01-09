package com.mll.automation.dashboard.reporting.controller;

import com.mll.automation.dashboard.reporting.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("report")
public class FileController {

    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadReport(@RequestBody MultipartFile file,
                                               @RequestParam(name = "fromDate") long fromDate,
                                               @RequestParam(name = "toDate") long toDate) throws IOException {
       try {
           log.info("Received file {} for  from {} and to date {} ", file.getOriginalFilename(), fromDate, toDate);
           fileService.processAndSaveFile(file, fromDate, toDate);
           return ResponseEntity.ok("Report processed successfully");
       } catch (Exception exception){
           String errorMessage = exception.getMessage();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error processing report: " + errorMessage);
       }
    }
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public void downloadTemplate(HttpServletResponse response) throws IOException{
        fileService.generateTemplate(response);
        log.info("Template sent");
    }
    @RequestMapping(value = "/export/parent-report/{fromDate}/{toDate}", method = RequestMethod.POST)
    public void exportParentReportByDateRange(@PathVariable("fromDate") Long fromDate,
                                              @PathVariable("toDate") Long toDate,
                                              HttpServletResponse response) throws IOException{
        fileService.exportReportByDateRange(fromDate, toDate, response);
    }
}