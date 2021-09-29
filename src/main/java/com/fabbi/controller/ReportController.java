package com.fabbi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.dto.ReportTotalMoneyDTO;
import com.fabbi.service.ReportService;

@Controller
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/reports/total-money")
    public String reportTotalMoney(Model model) {
		List<ReportTotalMoneyDTO> resultList = reportService.reportTotalMoney();

		model.addAttribute("resultList", resultList);
		
        return "report-money";
    }
	
	@GetMapping("/reports/best-sold")
    public String reportBestSold(Model model) {
		List<ReportBestSoldDTO> resultList = reportService.reportBestSold();
		
		model.addAttribute("resultList", resultList);
		
        return "report-best-sold";
    }
	
	@GetMapping("/reports/total-money/search")
    public String reportTotalMoneyByDate(@Param("searchDate") String searchDate, Model model) {
		ReportTotalMoneyDTO resultList = reportService.reportTotalMoneyByDate(searchDate);
		
		model.addAttribute("searchText", "searchText");
		model.addAttribute("resultListSearch", resultList);
		
        return "report-money";
    }
}
