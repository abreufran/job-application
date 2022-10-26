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

import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class ExcelWriteService {
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;

	public ExcelWriteService(UtilDateService utilDateService, UtilExcelService utilExcelService) {
		this.utilDateService = utilDateService;
		this.utilExcelService = utilExcelService;
	}

	protected boolean createExcel(String sheetName, int columnIndex, List<CellTypeWrapper> wrapperCellTypeList, List<RowWrapper> wrapperRowList, String filePaht)  {
    	XSSFWorkbook myWorkBook = null;
    	try {
    	
	    	myWorkBook = new XSSFWorkbook();
	    	this.addSheetToExcel(myWorkBook, sheetName, columnIndex, wrapperCellTypeList, wrapperRowList);
			
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
	
	protected void addEmptySheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<CellTypeWrapper> wrapperCellTypeList) {
		XSSFSheet sheet = myWorkBook.createSheet(sheetName);
    	System.out.println("addEmptySheetToExcel: " + sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
    	
    	System.out.println("Created");
    	
    	myWorkBook.setSheetOrder(sheetName, columnIndex);
		myWorkBook.setSelectedTab(columnIndex);
		myWorkBook.setActiveSheet(columnIndex);

		this.createHeaderRow(myWorkBook, sheet, wrapperCellTypeList);
		
		for (CellTypeWrapper cellTypeWrapper: wrapperCellTypeList) {	
			sheet.setColumnWidth(cellTypeWrapper.getColumnIndex(), cellTypeWrapper.getColumnWidth() * 256);
		}
	}
	
	protected void addSheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<CellTypeWrapper> wrapperCellTypeList, List<RowWrapper> wrapperRowList) {

    	XSSFSheet sheet = myWorkBook.createSheet(sheetName);
    	System.out.println("addSheetToExcel: " + sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
    	
    	System.out.println("Created");
    	
    	myWorkBook.setSheetOrder(sheetName, columnIndex);
		myWorkBook.setSelectedTab(0);
		myWorkBook.setActiveSheet(0);

		this.createHeaderRow(myWorkBook, sheet, wrapperCellTypeList);

		for (RowWrapper rowWrapper: wrapperRowList) {
			this.createBodyRow(myWorkBook, sheet, rowWrapper);
		}
		
		for (CellTypeWrapper cellTypeWrapper: wrapperCellTypeList) {	
			sheet.setColumnWidth(cellTypeWrapper.getColumnIndex(), cellTypeWrapper.getColumnWidth() * 256);
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
	
	protected void createHeaderRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<CellTypeWrapper> sheeCellTypeList) {
    	Row header = sheet.createRow(0);
    	System.out.println("createRow - Header: " + header.getRowNum());

		CellStyle headerStyle = myWorkBook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFFont font = ((XSSFWorkbook) myWorkBook).createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		for (CellTypeWrapper cellTypeWrapper: sheeCellTypeList) {
			String headerCellValue = cellTypeWrapper.getName();

			Cell headerCell = header.createCell(cellTypeWrapper.getColumnIndex());
			System.out.println("createCell - Header: " + headerCell.getColumnIndex());
			
			headerCell.setCellValue(headerCellValue);
			headerCell.setCellStyle(headerStyle);
		}
    }
	
	protected Row createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<CellTypeWrapper> sheeCellTypeList, int rowNumber) {
		Row row = sheet.createRow(rowNumber);
		System.out.println("createRow - Body: " + (row.getRowNum() + 1));
		this.utilExcelService.completeRow(row, myWorkBook, sheeCellTypeList);
		return row;
	}
    
	protected void createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, RowWrapper rowWrapper) {
    	Row row = sheet.createRow(rowWrapper.getRowNumber());
		System.out.println("createRow - Body: " + (row.getRowNum() + 1));

		for(CellWrapper cellWrapper: rowWrapper.getSheetCellList()) {
			Cell cell = row.createCell(cellWrapper.getSheetCellType().getColumnIndex(), cellWrapper.getSheetCellType().getCellType());
			System.out.println("createCell - Body: " + cell.getColumnIndex());
			
			HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
			if(cellWrapper.getSheetCellType().isRequired()) {
				horizontalAlignment = HorizontalAlignment.LEFT;
			}
			
			this.setBlankCellAndCellStyle(myWorkBook, cell, cellWrapper.getSheetCellType().isDate(), 
					cellWrapper.getSheetCellType().getCellType() == CellType.STRING, horizontalAlignment);
			
			if (cellWrapper.getSheetCellType().isDate() && cellWrapper.getCellValue() != null) {
				cell.setCellValue(this.utilDateService.getLocalDate(cellWrapper.getCellValue()));
				
			} else if (cellWrapper.getSheetCellType().getCellType() == CellType.FORMULA) {
				cell.setCellFormula(this.calculateFormula(cellWrapper, rowWrapper.getRowNumber()));
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				formulaEvaluator.evaluateFormulaCell(cell);

			} else {
				cell.setCellValue(cellWrapper.getCellValue());
			}
		}
    }
    
	protected String calculateFormula(CellWrapper cellWrapper, int rowNumber) {
    	String formula = cellWrapper.getSheetCellType().getSheetFormula().getFormula();
    	if(cellWrapper.getSheetCellType().getSheetFormula().getSheetFormulaValue() != null) {
	    	switch (cellWrapper.getSheetCellType().getSheetFormula().getSheetFormulaValue()) {
			case ROW_NUMBER:
				formula = formula.replaceAll(cellWrapper.getSheetCellType().getSheetFormula().getKey(), String.valueOf(rowNumber + 1));
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
