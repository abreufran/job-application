package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.Cell;

public class SheetCell {
	private SheetCellType sheetCellType;
	private String cellValue;
	private Cell cell;
	
	public SheetCell(
			SheetCellType sheetCellType, 
			String cellValue,
			Cell cell) {
		
		super();
		this.setSheetCellType(sheetCellType);
		this.setCell(cell);
		this.setCellValue(cellValue);
		
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public SheetCellType getSheetCellType() {
		return sheetCellType;
	}

	public void setSheetCellType(SheetCellType sheetCellType) {
		this.sheetCellType = sheetCellType;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	
}
