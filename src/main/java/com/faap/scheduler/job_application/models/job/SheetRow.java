package com.faap.scheduler.job_application.models.job;

import java.util.List;

public class SheetRow {
	private int rowNumber;
	private List<SheetCell> sheetCellList;
	
	public SheetRow(List<SheetCell> sheetCellList, int rowNumber) {
		super();
		this.sheetCellList = sheetCellList;
		this.rowNumber = rowNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<SheetCell> getSheetCellList() {
		return sheetCellList;
	}

	public void setSheetCellList(List<SheetCell> sheetCellList) {
		this.sheetCellList = sheetCellList;
	}

	@Override
	public String toString() {
		return "SheetRow [rowNumber=" + rowNumber + ", sheetCellList=" + sheetCellList + "]";
	}

	
	
	
}
