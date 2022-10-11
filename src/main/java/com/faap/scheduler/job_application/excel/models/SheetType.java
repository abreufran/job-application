package com.faap.scheduler.job_application.excel.models;

public enum SheetType {
	THINGS_TO_DO("Things to do");
	
	private String sheetName;
	
	private SheetType(String sheetName) {
		this.setSheetName(sheetName);
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
