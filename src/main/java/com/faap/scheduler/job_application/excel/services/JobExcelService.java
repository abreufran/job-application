package com.faap.scheduler.job_application.excel.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class JobExcelService extends AbstractApiExcelService {

	public JobExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		super(utilDateService, utilExcelService, 
				excelReadService,excelWriteService);
	}

	@Override
	public void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook, int rowNumber) {
		List<SheetCell> incompleteSheetCellList = this.getUtilExcelService().calculateIncompleteSheetCell(sheetCellList);
		
		for(SheetCell sheetCell: incompleteSheetCellList) {
			ThingToDoColumnType thingToDoColumnType = Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(sheetCell.getSheetCellType().getName())).findFirst().orElse(null);
			switch (thingToDoColumnType) {
			case ID:
				sheetCell.setCellValue(String.valueOf(rowNumber + 1));
				System.out.println("Completing Row: " + (rowNumber + 1) 
						+ " / columnName: " + sheetCell.getSheetCellType().getName() + " / Value: " + sheetCell.getCellValue());
				break;
			case INCIDENCE_DATE:
				sheetCell.setCellValue(this.getUtilDateService().getStrDate(LocalDate.now()));
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
