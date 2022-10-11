package com.faap.scheduler.job_application.excel.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelSheet {
	private Map<Integer,SheetCellType> sheetCellTypeHashMap;
	private SheetType sheetType;
	private List<SheetRow> sheetRowList;
	
	
	public ExcelSheet(SheetType sheetType) {
		this.sheetType = sheetType;
		this.sheetRowList = new ArrayList<>();
		this.sheetCellTypeHashMap = new HashMap<>();
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

	public Map<Integer,SheetCellType> getSheetCellTypeHashMap() {
		return sheetCellTypeHashMap;
	}


	public void setSheetCellTypeHashMap(Map<Integer,SheetCellType> sheetCellTypeHashMap) {
		this.sheetCellTypeHashMap = sheetCellTypeHashMap;
	}


	@Override
	public String toString() {
		return "ExcelSheet [sheetCellTypeHashMap=" + sheetCellTypeHashMap + ", sheetType=" + sheetType
				+ ", sheetRowList=" + sheetRowList + "]";
	}
	
	
}
