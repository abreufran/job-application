package com.faap.scheduler.job_application.excel.models;

import java.util.ArrayList;
import java.util.List;

public class ExcelSheet {
	private SheetType sheetType;
	private List<SheetRow> sheetRowList;
	
	
	public ExcelSheet(SheetType sheetType) {
		this.sheetType = sheetType;
		this.sheetRowList = new ArrayList<>();
	}


	public SheetType getSheetType() {
		return sheetType;
	}


	public void setSheetType(SheetType sheetType) {
		this.sheetType = sheetType;
	}


	public List<SheetRow> getSheetRowList() {
		return sheetRowList;
	}


	public void setSheetRowList(List<SheetRow> sheetRowList) {
		this.sheetRowList = sheetRowList;
	}
	
	
}
