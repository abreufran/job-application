package com.faap.scheduler.job_application.excel.models;

import java.util.ArrayList;
import java.util.List;

public class SheetWrapper {
	private String sheetName;
	private List<RowWrapper> wrapperRowList;
	
	
	public SheetWrapper(String sheetName) {
		this.setSheetName(sheetName);
		this.wrapperRowList = new ArrayList<>();
	}


	public List<RowWrapper> getRowWrapperList() {
		return wrapperRowList;
	}


	public void setRowWrapperList(List<RowWrapper> wrapperRowList) {
		this.wrapperRowList = wrapperRowList;
	}




	public String getSheetName() {
		return sheetName;
	}




	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	
}
