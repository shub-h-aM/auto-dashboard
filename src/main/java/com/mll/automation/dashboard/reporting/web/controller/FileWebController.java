package com.mll.automation.dashboard.reporting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("file")
public class FileWebController {
    @RequestMapping("/upload")
    public String uploadReport(Model model){
        model.addAttribute("title","Upload File");
        return "upload/upload-report";
    }
}
