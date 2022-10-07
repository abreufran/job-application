package com.faap.scheduler.job_application.excel.services;

import java.time.LocalDate;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.models.job.SheetCell;

public class JobExcelService extends AbstractExcelService {

	@Override
	public void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook) {
		for(SheetCell sheetCell: sheetCellList) {
			switch (sheetCell.getSheetCellType()) {
			case ID:
				sheetCell.getCell().setCellValue(String.valueOf(sheetCell.getRowNumber()));
				break;
			case INCIDENCE_DATE:
				sheetCell.getCell().setCellValue(LocalDate.now());
				break;
			case PRIORITY:
				sheetCell.getCell().setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;
			case THINGS_TO_DO:
				sheetCell.getCell().setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;	
			case CATEGORY:
				sheetCell.getCell().setCellValue(sheetCell.getSheetCellType().getDefaultValue().toString());
				break;	
			case STATUS:
				String formula = "IF(ISBLANK(C" + sheetCell.getRowNumber() + "),\"PENDING\",\"COMPLETE\")";
				sheetCell.getCell().setCellFormula(formula);
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				formulaEvaluator.evaluateFormulaCell(sheetCell.getCell());
				break;
			default:
				break;
			}
			System.out.println("Completing Row: " + sheetCell.getRowNumber() 
			+ " / Cell: " + sheetCell.getSheetCellType().getName() + " / Value: " + this.readCell(sheetCell.getCell()));
		}
		
	}
}
