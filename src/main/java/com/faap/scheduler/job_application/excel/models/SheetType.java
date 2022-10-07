package com.faap.scheduler.job_application.excel.models;

public enum SheetType {
	THINGS_TO_DO(0);
	
	private int sheetNumber;
	
	private SheetType(int sheetNumber) {
		this.setSheetNumber(sheetNumber);
	}

	public int getSheetNumber() {
		return sheetNumber;
	}

	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}
}
