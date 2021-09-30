package com.fabbi.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.dto.ReportTotalMoneyDTO;
import com.fabbi.service.ReportService;

@RestController
@RequestMapping(value = "/api/report")
public class ReportAPI {
	
	@Autowired
	private ReportService reportService;

	@RequestMapping(value = "/best-sold-product", method = RequestMethod.GET)
	public ResponseEntity<List<ReportBestSoldDTO>> getBestSoldReport() {

		try {
			List<ReportBestSoldDTO> result = reportService.reportBestSold();
			
			return new ResponseEntity<List<ReportBestSoldDTO>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportBestSoldDTO>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/total-money", method = RequestMethod.GET)
	public ResponseEntity<List<ReportTotalMoneyDTO>> getTotalMoneyReport() {

		try {
			List<ReportTotalMoneyDTO> result = reportService.reportTotalMoney();
			
			return new ResponseEntity<List<ReportTotalMoneyDTO>>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<ReportTotalMoneyDTO>>(HttpStatus.NOT_FOUND);
		}
	}
}
