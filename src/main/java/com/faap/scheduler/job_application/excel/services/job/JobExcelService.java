package com.faap.scheduler.job_application.excel.services.job;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.services.AbstractApiExcelService;
import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class JobExcelService extends AbstractApiExcelService {
	private static String THINGS_TO_DO_SHEET_NAME = "Things to do";
	private static String PERIODIC_TASKS_SHEET_NAME = "Periodic Tasks";

	public JobExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		super(utilDateService, utilExcelService, 
				excelReadService,excelWriteService);
	}
	
	public void loadPeriodicTasks(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		List<SheetCellType> sheetCellTypeList) {
		System.out.println("Load Periodic Task.");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(initialFilePath);

			ExcelSheet periodicTasksExcelSheet = this.readSheet(myWorkBook, PERIODIC_TASKS_SHEET_NAME,
					sheetCellTypeList);
			
			ExcelSheet thingsToDoExcelSheet = this.readSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME,
					sheetCellTypeList);
			
			List<SheetRow> periodicTasksSheetRowList = periodicTasksExcelSheet.getSheetRowList();
			List<SheetRow> thingsToDoSheetRowList = thingsToDoExcelSheet.getSheetRowList();
			
			this.deleteSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME);
			this.addSheetToExcel(myWorkBook, THINGS_TO_DO_SHEET_NAME, 0, sheetCellTypeList, sheetRowList);
			
			System.out.println("loadPeriodicTasks - Saving WorkBook.");
			this.writeExcel(myWorkBook, finalFilePath);
			
		} catch (Exception e) {
			e.printStackTrace();
			if (myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		
	}

	@Override
	public void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook, int rowNumber) {
		List<SheetCell> incompleteSheetCellList = this.utilExcelService.calculateIncompleteSheetCell(sheetCellList);
		
		for(SheetCell sheetCell: incompleteSheetCellList) {
			ThingToDoColumnType thingToDoColumnType = Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(sheetCell.getSheetCellType().getName())).findFirst().orElse(null);
			switch (thingToDoColumnType) {
			case ID:
				sheetCell.setCellValue(String.valueOf(rowNumber + 1));
				System.out.println("Completing Row: " + (rowNumber + 1) 
						+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Value: " + sheetCell.getCellValue());
				break;
			case INCIDENCE_DATE:
				sheetCell.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
				break;
			case PRIORITY:
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;
			case THINGS_TO_DO:
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;	
			case CATEGORY:
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;	
			case STATUS:
				break;
			default:
				break;
			}
			System.out.println("Completing Row: " + (rowNumber + 1) 
					+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Fixed Value: " + sheetCell.getCellValue());
		}
		
	}
	
	@Override
	public void updateCellIdValue(List<SheetRow> sheetRowList) {
		for (SheetRow sheetRow : sheetRowList) {
			SheetCell sheetCellId = sheetRow.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType().getName().equals(ThingToDoColumnType.ID.getName())).findFirst().orElse(null);
			
			if (!String.valueOf(sheetRow.getRowNumber() + 1).equals(sheetCellId.getCellValue())) {
				sheetCellId.setCellValue(String.valueOf(sheetRow.getRowNumber() + 1));
			}
		}
	}
}
