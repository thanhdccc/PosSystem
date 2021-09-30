package com.fabbi.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fabbi.dto.ReportBestSoldDTO;
import com.fabbi.dto.ReportTotalMoneyDTO;
import com.fabbi.repository.OrderDetailRepository;
import com.fabbi.repository.OrderRepository;
import com.fabbi.service.ReportService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Override
	public List<ReportTotalMoneyDTO> reportTotalMoney() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<ReportTotalMoneyDTO> resultDTO = null;
		
		log.info("######## Begin get report category ########");
		
		try {
			resultDTO = orderRepository.getTotalMoney();
		} catch (Exception e) {
			log.error("Error to execute query.....");
			return null;
		}
		
		if (resultDTO == null) {
			log.error("Error to get report.....");
			return null;
		}
		
		resultDTO.forEach(r -> r.setStrDate(dateFormat.format(r.getDate())));
		
		log.info("######## End get report category ########");
		
		return resultDTO;
	}

	@Override
	public List<ReportBestSoldDTO> reportBestSold() {
		List<ReportBestSoldDTO> resultDTO = null;
		
		log.info("######## Begin get report category ########");
		
		try {
			resultDTO = orderDetailRepository.getBestSold();
		} catch (Exception e) {
			log.error("Error to execute query.....");
		}
		
		if (resultDTO == null) {
			log.error("Error to get report.....");
		}
		
		log.info("######## End get report category ########");
		
		return resultDTO;
	}

	@Override
	public ReportTotalMoneyDTO reportTotalMoneyByDate(String searchDate) {
		ReportTotalMoneyDTO resultDTO = null;
		
		log.info("######## Begin get report category ########");
		
		try {
			resultDTO = orderRepository.getTotalMoneyByDate(searchDate);
		} catch (Exception e) {
			log.error("Error to execute query.....");
			return null;
		}
		
		if (resultDTO == null) {
			log.error("Error to get report.....");
			return null;
		}
		
		log.info("######## End get report category ########");
		
		return resultDTO;
	}

}
