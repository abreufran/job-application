package com.faap.scheduler.job_application.excel.dtos;

import com.faap.scheduler.job_application.excel.models.SheetType;

public class FieldEmptyFieldsRequest {
	private String filePath;
	private SheetType sheetType; 
	private int numberOfCells;
	private int requiredCellNumber;

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
	
	
}
