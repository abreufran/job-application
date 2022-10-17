package com.faap.scheduler.job_application.excel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.job.ValidCellListResponse;

public class ExcelReadService {
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;

	public ExcelReadService(UtilDateService utilDateService, UtilExcelService utilExcelService) {
		this.setUtilDateService(utilDateService);
		this.setUtilExcelService(utilExcelService);
	}

	public ExcelSheet readSheet(XSSFWorkbook myWorkBook, String sheetName, List<SheetCellType> sheeCellTypeList)
			throws Exception {

		ExcelSheet excelSheet = new ExcelSheet(sheetName);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();


		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			this.completeRow(row, myWorkBook, sheeCellTypeList);
			Iterator<Cell> cellIterator = row.cellIterator();

			List<Cell> cellList = new ArrayList<>();

			// For each row, iterate through each columns
			while (cellIterator.hasNext()) {
				cellList.add(cellIterator.next());
			}

			ValidCellListResponse validCellListResponse = this.validCellList(cellList, row.getRowNum(), sheeCellTypeList);

			if (validCellListResponse == ValidCellListResponse.OK) {
				if (row.getRowNum() > 0) {
					System.out.println("INFO: Row Number: " + (row.getRowNum() + 1) + " / Reading");
					List<SheetCell> sheetCellList = this.getSheetCellList(cellList,
							sheeCellTypeList, row.getRowNum());
					excelSheet.getSheetRowList().add(new SheetRow(sheetCellList, row.getRowNum()));
				}

			} else {
				if (validCellListResponse == ValidCellListResponse.INVALID_HEADER_CELL_LIST) {
					System.out.println("ERROR: Row Number: " + (row.getRowNum() + 1) + " / INVALID_HEADER_CELL_LIST");
					throw new Exception("INVALID_HEADER_CELL_LIST");
				}
				if (validCellListResponse == ValidCellListResponse.INVALID_REQUIRED_CELL) {
					System.out.println("WARNING: Row Number: " + (row.getRowNum() + 1) + " / INVALID_REQUIRED_CELL. DISCARTED ROW.");
				}
			}

		}

		this.checkDateCell(myWorkBook, excelSheet.getSheetRowList());

		return excelSheet;
	}
	
	private void checkDateCell(XSSFWorkbook myWorkBook, List<SheetRow> sheetRowList) {
		for (SheetRow sheetRow : sheetRowList) {
			List<SheetCell> sheetDateCells = sheetRow.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType().isDate()).collect(Collectors.toList());
			
			for (SheetCell sheetCell : sheetDateCells) {
				if (sheetCell.getSheetCellType().getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(sheetCell.getCell())) {

					if (sheetCell.getSheetCellType().isRequired()) {
						System.out.println("WARNING: RowNumber: " + (sheetRow.getRowNumber() + 1) 
								+ " / Required Date columnIdex: " + sheetCell.getSheetCellType().getColumnIndex() + " / Fixed.");
						sheetCell.setCellValue(this.getUtilDateService().getStrDate(LocalDate.now()));
					}

				}
			}
		}
	}
	
	private List<SheetCell> getSheetCellList(List<Cell> cellList, List<SheetCellType> sheetCellTypeList,
			int rowNumber) {
		List<SheetCell> sheetCellList = new ArrayList<>();
		for (Cell cell: cellList) {
			
			SheetCellType sheetCellType = sheetCellTypeList.stream().filter(ct -> ct.getColumnIndex() == cell.getColumnIndex()).findFirst().orElse(null);
					
			sheetCellList.add(new SheetCell(
					sheetCellType, 
					this.getUtilExcelService().readCell(cell), 
					cell));
		}
		return sheetCellList;
	}
	
	private ValidCellListResponse validCellList(List<Cell> cellList, int rowNumber, List<SheetCellType> sheeCellTypeList) {
		List<Cell> requiredCellList = cellList.stream().filter(c -> {
			return sheeCellTypeList.stream().anyMatch(ct -> ct.getColumnIndex() == c.getColumnIndex() && ct.isRequired());
		}).collect(Collectors.toList());
		
		if (!this.validCellList(requiredCellList)) {
			return ValidCellListResponse.INVALID_REQUIRED_CELL;
		}
		if (rowNumber == 0 && !this.validHeaderCellList(cellList, sheeCellTypeList)) {
			return ValidCellListResponse.INVALID_HEADER_CELL_LIST;
		}
		return ValidCellListResponse.OK;
	}
	
	private boolean validCellList(List<Cell> cellList) {
		for(Cell cell: cellList) {
			if(this.validCell(cell)) {
				String cellValue = this.getUtilExcelService().readCell(cell);
				if(cellValue != null && !cellValue.trim().equals("")) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || cell.getCellType() == CellType.NUMERIC
				|| cell.getCellType() == CellType.FORMULA;
	}
	
	private boolean validHeaderCellList(List<Cell> cellList, List<SheetCellType> sheetCellTypeList) {
		List<String> strCellList = this.getStrCellList(cellList);

		return sheetCellTypeList.stream().allMatch(sct -> {
			boolean match = strCellList.stream().filter(sc -> sct.getName().equals(sc)).findFirst().isPresent();
			return match;
		});

	}
	
	private List<String> getStrCellList(List<Cell> cellList) {
		List<String> strCellList = new ArrayList<>();

		for (Cell cell : cellList) {
			strCellList.add(this.getUtilExcelService().readCell(cell));
		}
		return strCellList;
	}
	
	private void completeRow(Row row, XSSFWorkbook myWorkBook, List<SheetCellType> sheeCellTypeList) {
		List<Cell> cellList = new ArrayList<>();
		Iterator<Cell> cellIterator = row.cellIterator();
		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {
			cellList.add(cellIterator.next());
		}

		for (SheetCellType sheetCellType: sheeCellTypeList) {
			if (this.existColumnIndex(cellList, sheetCellType.getColumnIndex())) {
				CellType cellType = sheetCellType.getCellType();
				Cell cell = row.createCell(sheetCellType.getColumnIndex(), cellType);
				
				HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
				if(sheetCellType.isRequired()) {
					horizontalAlignment = HorizontalAlignment.LEFT;
				}
				
				boolean isText = cell.getCellType() == CellType.STRING;
				
				this.setBlankCellAndCellStyle(myWorkBook, cell, sheetCellType.isDate(), isText, horizontalAlignment);
			}
		}
	}
	
	private boolean existColumnIndex(List<Cell> cellList, int columNumber) {
		return !cellList.stream().anyMatch(c -> c.getColumnIndex() == columNumber);
	}
	
	private void setBlankCellAndCellStyle(XSSFWorkbook myWorkBook, Cell cell, boolean isDate, boolean isText, HorizontalAlignment horizontalAlignment) {
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
	
    
    public XSSFWorkbook readExcel(String filePath) throws IOException {
		File myFile = new File(filePath);

		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkbook = new XSSFWorkbook(fis);

		fis.close();
		myFile = null;
		fis = null;

		return myWorkbook;
	}

	public UtilDateService getUtilDateService() {
		return utilDateService;
	}

	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}

	public UtilExcelService getUtilExcelService() {
		return utilExcelService;
	}

	public void setUtilExcelService(UtilExcelService utilExcelService) {
		this.utilExcelService = utilExcelService;
	}
}
