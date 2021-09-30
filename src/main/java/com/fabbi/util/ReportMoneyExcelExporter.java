package com.fabbi.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fabbi.dto.ReportTotalMoneyDTO;

public class ReportMoneyExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<ReportTotalMoneyDTO> reportList;

	public ReportMoneyExcelExporter(List<ReportTotalMoneyDTO> reportList) {
		this.reportList = reportList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Money");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Date", style);
		createCell(row, 1, "Total Money", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (ReportTotalMoneyDTO item : reportList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, item.getStrDate(), style);
			createCell(row, columnCount++, item.getTotal(), style);

		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}
}
