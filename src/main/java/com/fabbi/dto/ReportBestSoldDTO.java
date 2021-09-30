package com.fabbi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportBestSoldDTO {
	
	private String name;

	private Integer total;

	public ReportBestSoldDTO(String name, Integer total) {
		this.name = name;
		this.total = total;
	}
}
