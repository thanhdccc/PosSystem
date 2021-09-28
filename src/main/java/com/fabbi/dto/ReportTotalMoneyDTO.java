package com.fabbi.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportTotalMoneyDTO {

	private Integer total;

	private Date date;

	public ReportTotalMoneyDTO(Integer total, Date date) {
		this.total = total;
		this.date = date;
	}
}
