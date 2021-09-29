package com.fabbi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportBestSoldDTO {

	private Integer total;

	private String name;

	public ReportBestSoldDTO(Integer total, String name) {
		this.total = total;
		this.name = name;
	}
}
