package com.faap.scheduler.job_application.excel.services;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.file.services.GoalService;

public class DoneExcelService {
	
	private ExcelReadService excelReadService;
	private GoalService goalService;
	
	public DoneExcelService(ExcelReadService excelReadService, GoalService goalService) {
		this.excelReadService = excelReadService;
		this.goalService = goalService;
	}

	public void exportDoneTask(String initialFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		XSSFWorkbook myWorkBook = null;
		int customerId = 1;
		String customerFirstName = "System";
		String customerEmail = "goal-support@gmail.com";
		String systemChannelName = "System";
		String webChannelName = "Web";
		String formatDateToRequest = "yyyyMMdd";
		
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);
			
			SheetWrapper sheetWrapper = this.excelReadService.readSheet(myWorkBook, sheetName, wrapperCellTypeList);
			
			/*
			for(RowWrapper rowWrapper: sheetWrapper.getRowWrapperList()) {
				System.out.println("RowNumber: " + rowWrapper.getRowNumber());
				for(CellWrapper cellWrapper: rowWrapper.getCellWrapperList()) {
					System.out.println("Cell Name / Index / Value: " + cellWrapper.getCellTypeWrapper().getName() 
							+ " / " + cellWrapper.getCellTypeWrapper().getColumnIndex()
							+ " / " + cellWrapper.getCellValue());
				}
			}*/
			
			goalService.exportDoneRowWrapperList(sheetWrapper.getRowWrapperList(), customerId, customerFirstName, customerEmail,
					systemChannelName, formatDateToRequest,
					webChannelName);
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void exportDailyTaskTask(String initialFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		XSSFWorkbook myWorkBook = null;
		int customerId = 1;
		String customerFirstName = "System";
		String customerEmail = "goal-support@gmail.com";
		String systemChannelName = "System";
		String webChannelName = "Web";
		String formatDateToRequest = "yyyyMMdd";
		String disciplineName = "None";
		
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);
			
			SheetWrapper sheetWrapper = this.excelReadService.readSheet(myWorkBook, sheetName, wrapperCellTypeList);
			
			/*
			for(RowWrapper rowWrapper: sheetWrapper.getRowWrapperList()) {
				System.out.println("RowNumber: " + rowWrapper.getRowNumber());
				for(CellWrapper cellWrapper: rowWrapper.getCellWrapperList()) {
					System.out.println("Cell Name / Index / Value: " + cellWrapper.getCellTypeWrapper().getName() 
							+ " / " + cellWrapper.getCellTypeWrapper().getColumnIndex()
							+ " / " + cellWrapper.getCellValue());
				}
			}*/
			
			goalService.exportDailyTaskRowWrapperList(sheetWrapper.getRowWrapperList(), customerId, customerFirstName, customerEmail,
					systemChannelName, formatDateToRequest,
					disciplineName, webChannelName);
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void exportPeriodicTask(String initialFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		XSSFWorkbook myWorkBook = null;
		int customerId = 1;
		String customerFirstName = "System";
		String customerEmail = "goal-support@gmail.com";
		String systemChannelName = "System";
		String webChannelName = "Web";
		String formatDateToRequest = "yyyyMMdd";
		String disciplineName = "None";
		
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);
			
			SheetWrapper sheetWrapper = this.excelReadService.readSheet(myWorkBook, sheetName, wrapperCellTypeList);
			
			/*
			for(RowWrapper rowWrapper: sheetWrapper.getRowWrapperList()) {
				System.out.println("RowNumber: " + rowWrapper.getRowNumber());
				for(CellWrapper cellWrapper: rowWrapper.getCellWrapperList()) {
					System.out.println("Cell Name / Index / Value: " + cellWrapper.getCellTypeWrapper().getName() 
							+ " / " + cellWrapper.getCellTypeWrapper().getColumnIndex()
							+ " / " + cellWrapper.getCellValue());
				}
			}*/
			
			goalService.exportPeriodicRowWrapperList(sheetWrapper.getRowWrapperList(), customerId, customerFirstName, customerEmail,
					systemChannelName, formatDateToRequest,
					disciplineName, webChannelName);
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
