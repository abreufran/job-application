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
		String systemChannelName = "System";
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
			
			goalService.exportRowWrapperList(sheetWrapper.getRowWrapperList(), customerId, systemChannelName);
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
