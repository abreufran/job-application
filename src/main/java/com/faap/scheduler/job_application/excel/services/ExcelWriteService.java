package com.faap.scheduler.job_application.excel.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class ExcelWriteService {
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;

	public ExcelWriteService(UtilDateService utilDateService, UtilExcelService utilExcelService) {
		this.utilDateService = utilDateService;
		this.utilExcelService = utilExcelService;
	}

	protected boolean createExcel(String sheetName, int columnIndex, List<SheetCellType> sheetCellTypeList, List<SheetRow> sheetRowList, String filePaht)  {
    	XSSFWorkbook myWorkBook = null;
    	try {
    	
	    	myWorkBook = new XSSFWorkbook();
	    	this.addSheetToExcel(myWorkBook, sheetName, columnIndex, sheetCellTypeList, sheetRowList);
			
	    	this.writeExcel(myWorkBook, filePaht);
	    	
	    	return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
	
	protected void addEmptySheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<SheetCellType> sheetCellTypeList) {
		XSSFSheet sheet = myWorkBook.createSheet(sheetName);
    	System.out.println("addEmptySheetToExcel: " + sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
    	
    	System.out.println("Created");
    	
    	myWorkBook.setSheetOrder(sheetName, columnIndex);
		myWorkBook.setSelectedTab(columnIndex);
		myWorkBook.setActiveSheet(columnIndex);

		this.createHeaderRow(myWorkBook, sheet, sheetCellTypeList);
		
		for (SheetCellType sheetCellType: sheetCellTypeList) {	
			sheet.setColumnWidth(sheetCellType.getColumnIndex(), sheetCellType.getColumnWidth() * 256);
		}
	}
	
	protected void addSheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<SheetCellType> sheetCellTypeList, List<SheetRow> sheetRowList) {

    	XSSFSheet sheet = myWorkBook.createSheet(sheetName);
    	System.out.println("addSheetToExcel: " + sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
    	
    	System.out.println("Created");
    	
    	myWorkBook.setSheetOrder(sheetName, columnIndex);
		myWorkBook.setSelectedTab(0);
		myWorkBook.setActiveSheet(0);

		this.createHeaderRow(myWorkBook, sheet, sheetCellTypeList);

		for (SheetRow sheetRow: sheetRowList) {
			this.createBodyRow(myWorkBook, sheet, sheetRow);
		}
		
		for (SheetCellType sheetCellType: sheetCellTypeList) {	
			sheet.setColumnWidth(sheetCellType.getColumnIndex(), sheetCellType.getColumnWidth() * 256);
		}

    }
	
	protected void deleteSheet(XSSFWorkbook myWorkBook, String sheetName) {
		int index = 0;
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
		if (mySheet != null) {
			index = myWorkBook.getSheetIndex(mySheet);
			myWorkBook.removeSheetAt(index);
		}
	}
	
	protected void createHeaderRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<SheetCellType> sheeCellTypeList) {
    	Row header = sheet.createRow(0);
    	System.out.println("createRow - Header: " + header.getRowNum());

		CellStyle headerStyle = myWorkBook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFFont font = ((XSSFWorkbook) myWorkBook).createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		for (SheetCellType sheetCellType: sheeCellTypeList) {
			String headerCellValue = sheetCellType.getName();

			Cell headerCell = header.createCell(sheetCellType.getColumnIndex());
			System.out.println("createCell - Header: " + headerCell.getColumnIndex());
			
			headerCell.setCellValue(headerCellValue);
			headerCell.setCellStyle(headerStyle);
		}
    }
	
	protected Row createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<SheetCellType> sheeCellTypeList) {
		Row row = sheet.createRow(sheet.getLastRowNum());
		System.out.println("createRow - Body: " + row.getRowNum());
		this.utilExcelService.completeRow(row, myWorkBook, sheeCellTypeList);
		return row;
	}
    
	protected void createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, SheetRow sheetRow) {
    	Row row = sheet.createRow(sheetRow.getRowNumber());
		System.out.println("createRow - Body: " + row.getRowNum());

		for(SheetCell sheetCell: sheetRow.getSheetCellList()) {
			Cell cell = row.createCell(sheetCell.getSheetCellType().getColumnIndex(), sheetCell.getSheetCellType().getCellType());
			System.out.println("createCell - Body: " + cell.getColumnIndex());
			
			HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
			if(sheetCell.getSheetCellType().isRequired()) {
				horizontalAlignment = HorizontalAlignment.LEFT;
			}
			
			this.setBlankCellAndCellStyle(myWorkBook, cell, sheetCell.getSheetCellType().isDate(), 
					sheetCell.getSheetCellType().getCellType() == CellType.STRING, horizontalAlignment);
			
			if (sheetCell.getSheetCellType().isDate() && sheetCell.getCellValue() != null) {
				cell.setCellValue(this.utilDateService.getLocalDate(sheetCell.getCellValue()));
				
			} else if (sheetCell.getSheetCellType().getCellType() == CellType.FORMULA) {
				cell.setCellFormula(this.calculateFormula(sheetCell, sheetRow.getRowNumber()));
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				formulaEvaluator.evaluateFormulaCell(cell);

			} else {
				cell.setCellValue(sheetCell.getCellValue());
			}
		}
    }
    
	protected String calculateFormula(SheetCell sheetCell, int rowNumber) {
    	String formula = sheetCell.getSheetCellType().getSheetFormula().getFormula();
    	if(sheetCell.getSheetCellType().getSheetFormula().getSheetFormulaValue() != null) {
	    	switch (sheetCell.getSheetCellType().getSheetFormula().getSheetFormulaValue()) {
			case ROW_NUMBER:
				formula = formula.replaceAll(sheetCell.getSheetCellType().getSheetFormula().getKey(), String.valueOf(rowNumber + 1));
				break;
			default:
				break;
			}
    	}
		return formula;
    }
    
	protected void setBlankCellAndCellStyle(XSSFWorkbook myWorkBook, Cell cell, boolean isDate, boolean isText, HorizontalAlignment horizontalAlignment) {
		cell.setBlank();
		CellStyle style = myWorkBook.createCellStyle();
		style.setAlignment(horizontalAlignment);
		if(isText) {
			style.setWrapText(true);
		}

		if (isDate) {
			CreationHelper createHelper = myWorkBook.getCreationHelper();
			style.setDataFormat(createHelper.createDataFormat().getFormat(UtilDateService.WRITE_EXCEL_DATE_FORMAT));
		}
		cell.setCellStyle(style);
	}
    
	protected void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		File myFile = new File(filePath);

		FileOutputStream outputStream = new FileOutputStream(myFile);

		myWorkBook.write(outputStream);

		outputStream.close();
		myFile = null;
		outputStream = null;
	}
}
