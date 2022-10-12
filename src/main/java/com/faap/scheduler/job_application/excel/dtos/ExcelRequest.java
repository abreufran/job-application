package com.faap.scheduler.job_application.excel.dtos;

import java.util.List;

import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetType;

public class ExcelRequest {
	private String filePath;
	private SheetType sheetType; 
	private List<SheetCellType> sheetCellTypeList;
	private int cellNumberToSort;
	private int cellNumberToFilter;
	private String tokenToFilter;

	
	
	public ExcelRequest(String filePath, SheetType sheetType, List<SheetCellType> sheetCellTypeList, int cellNumberToSort, int cellNumberToFilter,
			String tokenToFilter) {
		super();
		this.filePath = filePath;
		this.sheetType = sheetType;
		this.setSheetCellTypeList(sheetCellTypeList);
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

	public List<SheetCellType> getSheetCellTypeList() {
		return sheetCellTypeList;
	}

	public void setSheetCellTypeList(List<SheetCellType> sheetCellTypeList) {
		this.sheetCellTypeList = sheetCellTypeList;
	}
	
	
}
