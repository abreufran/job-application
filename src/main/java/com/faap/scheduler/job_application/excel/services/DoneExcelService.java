package com.faap.scheduler.job_application.excel.services;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;

public class DoneExcelService {
	
	private ExcelReadService excelReadService;
	
	public DoneExcelService(ExcelReadService excelReadService) {
		this.excelReadService = excelReadService;
	}

	public void exportDoneTask(String initialFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);
			
			SheetWrapper sheetWrapper = this.excelReadService.readSheet(myWorkBook, sheetName, wrapperCellTypeList);
			
			for(RowWrapper rowWrapper: sheetWrapper.getRowWrapperList()) {
				System.out.println("RowNumber: " + rowWrapper.getRowNumber());
				for(CellWrapper cellWrapper: rowWrapper.getCellWrapperList()) {
					System.out.println("Cell Name / Index / Value: " + cellWrapper.getCellTypeWrapper().getName() 
							+ " / " + cellWrapper.getCellTypeWrapper().getColumnIndex()
							+ " / " + cellWrapper.getCellValue());
				}
			}
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
