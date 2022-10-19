package com.faap.scheduler.job_application.excel.services.job;

import java.time.LocalDate;
import java.util.Arrays;

import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;

public class UtilJobExcelService {

	public SheetRow mappingThingsToDoSheetRow(SheetRow periodicTasksSheetRow) {
		SheetCellType sheetCellType = 
		
		SheetCell sheetCell = new SheetCell();
		
		SheetRow sheetRow = new SheetRow();
		
		return null;
	}
	
	public SheetCellType getThingsToDoSheetCellType(SheetCellType periodicTaskSheetCellType) {
		ThingToDoColumnType thingToDoColumnType = Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(sheetCell.getSheetCellType().getName())).findFirst().orElse(null);
		switch (periodicTaskSheetCellType.getName()) {
		case ID:
			sheetCell.setCellValue(String.valueOf(rowNumber + 1));
			System.out.println("Completing Row: " + (rowNumber + 1) 
					+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Value: " + sheetCell.getCellValue());

		default:
			break;
		}
		
	}
}
