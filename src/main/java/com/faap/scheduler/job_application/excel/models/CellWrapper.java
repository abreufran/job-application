package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.Cell;

public class CellWrapper {
	private CellTypeWrapper cellTypeWrapper;
	private String cellValue;
	private Cell cell;
	
	public CellWrapper(
			CellTypeWrapper cellTypeWrapper, 
			String cellValue,
			Cell cell) {
		
		super();
		this.setSheetCellType(cellTypeWrapper);
		this.setCell(cell);
		this.setCellValue(cellValue);
		
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public CellTypeWrapper getSheetCellType() {
		return cellTypeWrapper;
	}

	public void setSheetCellType(CellTypeWrapper cellTypeWrapper) {
		this.cellTypeWrapper = cellTypeWrapper;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	
}
