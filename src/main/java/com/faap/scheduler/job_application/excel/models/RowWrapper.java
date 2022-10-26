package com.faap.scheduler.job_application.excel.models;

import java.util.List;

public class RowWrapper {
	private int rowNumber;
	private List<CellWrapper> wrapperCellList;
	
	public RowWrapper(List<CellWrapper> wrapperCellList, int rowNumber) {
		super();
		this.wrapperCellList = wrapperCellList;
		this.rowNumber = rowNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<CellWrapper> getSheetCellList() {
		return wrapperCellList;
	}

	public void setSheetCellList(List<CellWrapper> wrapperCellList) {
		this.wrapperCellList = wrapperCellList;
	}

	@Override
	public String toString() {
		return "SheetRow [rowNumber=" + rowNumber + ", wrapperCellList=" + wrapperCellList + "]";
	}

	
	
	
}
