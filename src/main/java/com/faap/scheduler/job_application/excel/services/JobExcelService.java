package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class JobExcelService extends AbstractApiExcelService {
	private static String THINGS_TO_DO_SHEET_NAME = "Things to do";
	private static String PERIODIC_TASKS_SHEET_NAME = "Periodic Tasks";
	public static int COLUMN_INDEX_TO_SORT = 4;
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";

	public JobExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		super(utilDateService, utilExcelService, 
				excelReadService,excelWriteService);
	}
	
	public void loadThingsToDoSheet(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		List<SheetCellType> initialSheetCellTypeList, List<SheetCellType> finalSheetCellTypeList) {
		System.out.println("Load Periodic Task.");
		WorkbookResponse workbookResponse = null;
		XSSFWorkbook myWorkBook = null;
		try {
			workbookResponse = this.fillSortSheet(initialFilePath, finalFilePath, 
					THINGS_TO_DO_SHEET_NAME, finalSheetCellTypeList, COLUMN_INDEX_TO_SORT, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
			
			if(workbookResponse.isSuccess()) {
				myWorkBook = workbookResponse.getMyWorkBook();
				
				ExcelSheet initialExcelSheet = this.readSheet(myWorkBook, PERIODIC_TASKS_SHEET_NAME,
						initialSheetCellTypeList);
				
				ExcelSheet finalExcelSheet = this.readSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME,
						finalSheetCellTypeList);
				
				
				for(SheetRow initialSheetRow: initialExcelSheet.getSheetRowList()) {
					//String estimatedDate = null;
					String priority = null;
					String thingsToDo = null;
					String category = null;
					//String periodicity = null;
					//String weekday = null;
					
					for(SheetCell initialSheetCell: initialSheetRow.getSheetCellList()) {
						PeriodicTaskColumnType initialColumnType = Arrays.asList(PeriodicTaskColumnType.values()).stream().filter(ptct -> ptct.getName().equals(initialSheetCell.getSheetCellType().getName())).findFirst().orElse(null);
						switch (initialColumnType) {
						case TASK:
							thingsToDo = initialSheetCell.getCellValue();
							break;
						case PRIORITY:
							priority = initialSheetCell.getCellValue();
							break;
						case CATEGORY: 
							category = initialSheetCell.getCellValue();
							break;
						case PERIODICITY: 
							//periodicity = initialSheetCell.getCellValue();
							break;
						case WEEKDAY: 
							//weekday = initialSheetCell.getCellValue();
							break;	
						default:
							break;
						}
					}
					
					if(!this.existSheetCell(finalExcelSheet.getSheetRowList(), thingsToDo, this.utilDateService.getStrDate(LocalDate.now()))) {
						Row row = this.createBodyRow(myWorkBook, myWorkBook.getSheet(THINGS_TO_DO_SHEET_NAME), finalSheetCellTypeList);
						
						List<Cell> cellList = this.utilExcelService.getCellList(row, finalSheetCellTypeList);
						
						SheetRow sheetRow = this.excelReadService.addSheetRow(finalExcelSheet, cellList, finalSheetCellTypeList, row);
						
						for(SheetCell sheetCell: sheetRow.getSheetCellList()) {
							this.completeSheetCell(sheetCell, row.getRowNum(), null, priority, thingsToDo, category);
						}
					}
					
					
				}
				
				this.deleteSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME);
				this.addSheetToExcel(myWorkBook, THINGS_TO_DO_SHEET_NAME, 0, finalSheetCellTypeList, finalExcelSheet.getSheetRowList());
				
				System.out.println("loadPeriodicTasks - Saving WorkBook.");
				this.writeExcel(myWorkBook, finalFilePath);	
			}
			
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
			this.completeSheetCell(sheetCell, rowNumber, null, null, null, null);
		}
		
	}
	
	private void completeSheetCell(
			SheetCell sheetCell, 
			int rowNumber, 
			String estimatedDate, 
			String priority, 
			String thingsToDo, 
			String category) {
		
		ThingToDoColumnType thingToDoColumnType = this.getThingToDoColumnType(sheetCell);
		switch (thingToDoColumnType) {
		case ID:
			sheetCell.setCellValue(String.valueOf(rowNumber + 1));
			System.out.println("Completing Row: " + (rowNumber + 1) 
					+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Value: " + sheetCell.getCellValue());
			break;
		case INCIDENCE_DATE:
			sheetCell.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
			break;
		case ESTIMATED_DATE:
			if(estimatedDate == null) {
				sheetCell.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
			}
			else {
				sheetCell.setCellValue(estimatedDate);
			}
			break;	
		case PRIORITY:
			if(priority == null) {
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
			}
			else {
				sheetCell.setCellValue(priority);
			}
			break;
		case THINGS_TO_DO:
			if(thingsToDo == null) {
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
			}
			else {
				sheetCell.setCellValue(thingsToDo);
			}
			break;	
		case CATEGORY:
			if(category == null) {
				sheetCell.setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
			}
			else {
				sheetCell.setCellValue(category);
			}
			break;	
		case STATUS:
			break;
		default:
			break;
		}
		System.out.println("Completing Row: " + (rowNumber + 1) 
				+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Fixed Value: " + sheetCell.getCellValue());
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
	
	private boolean existSheetCell(List<SheetRow> sheetRowList, String thingsToDo, String incidenceDate) {
		return sheetRowList.stream().anyMatch(sr -> {
			boolean thingsToDoMatched = false;
			boolean incidenceDateMatched = false;
			for(SheetCell sheetCell: sr.getSheetCellList()) {
				ThingToDoColumnType thingToDoColumnType = this.getThingToDoColumnType(sheetCell);
				if(thingToDoColumnType == ThingToDoColumnType.THINGS_TO_DO && thingsToDo.equals(sheetCell.getCellValue())) {
					thingsToDoMatched = true;
				}
				
				if(thingToDoColumnType == ThingToDoColumnType.INCIDENCE_DATE && incidenceDate.equals(sheetCell.getCellValue())) {
					incidenceDateMatched = true;
				}
			}
			if(thingsToDoMatched && incidenceDateMatched) {
				return true;
			}
			else {
				return false;
			}
		});
	}
	
	private ThingToDoColumnType getThingToDoColumnType(SheetCell sheetCell) {
		return Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(sheetCell.getSheetCellType().getName())).findFirst().orElse(null);
	}
}
