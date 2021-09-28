package com.fabbi.service;

import java.util.List;

import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.dto.ReportTotalMoneyDTO;

public interface ReportService {

	List<ReportTotalMoneyDTO> reportTotalMoney();
	
	ReportTotalMoneyDTO reportTotalMoneyByDate(String searchDate);
	
	List<ReportBestSoldDTO> reportBestSold();
}
