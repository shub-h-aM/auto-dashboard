package com.mll.automation.dashboard.reporting.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AutomationDashboardWebController {
    @RequestMapping("/")
    public String automationDashboardHome(Model model) {
        model.addAttribute("title", "Automation Dashboard");
        return "base/automation-dashboard-home";
    }
}
