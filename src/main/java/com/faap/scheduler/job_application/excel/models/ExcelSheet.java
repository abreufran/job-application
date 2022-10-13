package com.faap.scheduler.job_application.excel.models;

import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {
	private String sheetName;
	private List<SheetRow> sheetRowList;
	
	
	public ExcelSheet(String sheetName) {
		this.setSheetName(sheetName);
		this.sheetRowList = new ArrayList<>();
	}


	public List<SheetRow> getSheetRowList() {
		return sheetRowList;
	}


	public void setSheetRowList(List<SheetRow> sheetRowList) {
		this.sheetRowList = sheetRowList;
	}




	public String getSheetName() {
		return sheetName;
	}




	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	
}
