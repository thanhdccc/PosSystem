package com.fabbi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fabbi.constant.Constant;
import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.dto.ReportTotalMoneyDTO;
import com.fabbi.service.ReportService;
import com.fabbi.util.ReportBestSoldProductExcelExporter;
import com.fabbi.util.ReportMoneyExcelExporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
    public String reportTotalMoneyByDate(@Param("searchDate") String searchDate
    		, Model model
    		, RedirectAttributes redirectAttributes) {
		
		ReportTotalMoneyDTO resultList = reportService.reportTotalMoneyByDate(searchDate);
		
		if (resultList == null) {
			redirectAttributes.addFlashAttribute("messageFail", "Not Found.");
			
		} else {
			
			model.addAttribute("searchText", "searchText");
			model.addAttribute("resultListSearch", resultList);
		}
		
        return "report-money";
    }
	
	@GetMapping("/reports/total-money/export-excel")
	public void exportTotalMoneyToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");

		response.setHeader(Constant.EXPORT_EXCEL_HEADER_NAME, Constant.EXPORT_EXCEL_HEADER_VALUE_TOTAL_MONEY);
		
		List<ReportTotalMoneyDTO> resultList = reportService.reportTotalMoney();
		
		ReportMoneyExcelExporter excelExporter = new ReportMoneyExcelExporter(resultList);
		
		excelExporter.export(response);
	}
	
	@GetMapping("/reports/total-money/export-pdf")
	public void exportTotalMoneyToPDF(HttpServletResponse response) throws IOException, JRException {
		response.setContentType("application/octet-stream");

		response.setHeader(Constant.EXPORT_EXCEL_HEADER_NAME, Constant.EXPORT_EXCEL_HEADER_VALUE_TOTAL_MONEY_PDF);
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource("reports/report_total_money.jrxml").getInputStream());
		
		List<ReportTotalMoneyDTO> resultList = reportService.reportTotalMoney();
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(resultList);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", userDetail.getUsername());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}
	
	@GetMapping("/reports/best-sold/export-excel")
	public void exportBestSoldToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");

		response.setHeader(Constant.EXPORT_EXCEL_HEADER_NAME, Constant.EXPORT_EXCEL_HEADER_VALUE_PRODUCT);
		
		List<ReportBestSoldDTO> resultList = reportService.reportBestSold();
		
		ReportBestSoldProductExcelExporter excelExporter = new ReportBestSoldProductExcelExporter(resultList);
		
		excelExporter.export(response);
	}
	
	@GetMapping("/reports/best-sold/export-pdf")
	public void exportBestSoldToPDF(HttpServletResponse response) throws IOException, JRException {
		response.setContentType("application/octet-stream");

		response.setHeader(Constant.EXPORT_EXCEL_HEADER_NAME, Constant.EXPORT_EXCEL_HEADER_VALUE_PRODUCT_PDF);
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		
		JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource("reports/report_best_sold_product.jrxml").getInputStream());
		
		List<ReportBestSoldDTO> resultList = reportService.reportBestSold();
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(resultList);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", userDetail.getUsername());
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}
}
