package com.faap.scheduler.job_application.models.job;

import org.apache.poi.ss.usermodel.Cell;

public class SheetCell {
	private int rowNumber;
	private SheetCellType sheetCellType;
	private Cell cell;
	
	public SheetCell(SheetCellType sheetCellType, Cell cell, int rowNumber) {
		super();
		this.sheetCellType = sheetCellType;
		this.cell = cell;
		this.rowNumber = rowNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
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

	@Override
	public String toString() {
		return "SheetCell [rowNumber=" + rowNumber + ", sheetCellType=" + sheetCellType + ", cell=" + cell + "]";
	}
	
	
	
	
	
}
