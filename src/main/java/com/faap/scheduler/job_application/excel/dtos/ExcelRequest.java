package com.faap.scheduler.job_application.excel.dtos;

import com.faap.scheduler.job_application.excel.models.SheetType;

public class ExcelRequest {
	private String filePath;
	private SheetType sheetType; 
	private int numberOfCells;
	private int requiredCellNumber;
	private int cellNumberToSort;
	private int cellNumberToFilter;
	private String tokenToFilter;

	
	
	public ExcelRequest(String filePath, SheetType sheetType, int numberOfCells, 
			int requiredCellNumber, int cellNumberToSort, int cellNumberToFilter,
			String tokenToFilter) {
		super();
		this.filePath = filePath;
		this.sheetType = sheetType;
		this.numberOfCells = numberOfCells;
		this.requiredCellNumber = requiredCellNumber;
		this.setCellNumberToSort(cellNumberToSort);
		this.setCellNumberToFilter(cellNumberToFilter);
		this.setTokenToFilter(tokenToFilter);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public SheetType getSheetType() {
		return sheetType;
	}

	public void setSheetType(SheetType sheetType) {
		this.sheetType = sheetType;
	}

	public int getNumberOfCells() {
		return numberOfCells;
	}

	public void setNumberOfCells(int numberOfCells) {
		this.numberOfCells = numberOfCells;
	}

	public int getRequiredCellNumber() {
		return requiredCellNumber;
	}

	public void setRequiredCellNumber(int requiredCellNumber) {
		this.requiredCellNumber = requiredCellNumber;
	}

	public int getCellNumberToSort() {
		return cellNumberToSort;
	}

	public void setCellNumberToSort(int cellNumberToSort) {
		this.cellNumberToSort = cellNumberToSort;
	}

	public int getCellNumberToFilter() {
		return cellNumberToFilter;
	}

	public void setCellNumberToFilter(int cellNumberToFilter) {
		this.cellNumberToFilter = cellNumberToFilter;
	}

	public String getTokenToFilter() {
		return tokenToFilter;
	}

	public void setTokenToFilter(String tokenToFilter) {
		this.tokenToFilter = tokenToFilter;
	}
	
	
}
