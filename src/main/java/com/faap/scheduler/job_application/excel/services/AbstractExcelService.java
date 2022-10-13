package com.faap.scheduler.job_application.excel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters;

import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.job.ValidCellListResponse;

public abstract class AbstractExcelService {
	public static int INITIAL_ROW_NUMBER = 1;
	private UtilDateService utilDateService;

	public AbstractExcelService(UtilDateService utilDateService) {
		this.setUtilDateService(utilDateService);
	}

	public abstract void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook, int rowNumber);

	public XSSFWorkbook readExcel(String filePath) throws IOException {
		File myFile = new File(filePath);

		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkbook = new XSSFWorkbook(fis);

		fis.close();
		myFile = null;
		fis = null;

		return myWorkbook;
	}

	protected void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		File myFile = new File(filePath);

		FileOutputStream outputStream = new FileOutputStream(myFile);

		myWorkBook.write(outputStream);

		outputStream.close();
		myFile = null;
		outputStream = null;
	}

	public boolean rebuildSheet(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		System.out.println("rebuildSheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(filePath);

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			this.deleteSheet(myWorkBook, sheetName);
			
			this.createSheet(myWorkBook, excelSheet, 
					excelSheet.getSheetRowList().size(), sheetCellTypeList);
			
			System.out.println("rebuildSheet - Saving WorkBook.");
			this.writeExcel(myWorkBook, filePath);

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

	

	public boolean fillEmptyFields(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		System.out.println("Fill empty fields. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(filePath);

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			List<SheetRow> incompleteSheetRowList = this.calculateIncompleteSheetRowList(excelSheet.getSheetRowList());

			//List<SheetCell> incompleteSheetCellList = this.calculateIncompleteSheetCell(incompleteSheetRowList);

			for(SheetRow sheetRow: incompleteSheetRowList) {
				this.completeSheetCellList(sheetRow.getSheetCellList(), myWorkBook, sheetRow.getRowNumber());
			}

			if (incompleteSheetRowList.size() > 0) {

				System.out.println("fillEmptyFields - Saving WorkBook.");
				this.writeExcel(myWorkBook, filePath);

			}

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

	public boolean sortSheet(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {
		System.out.println("Sort Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(filePath);

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			Comparator<SheetRow> priorityComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(columnIndexToSort).getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(columnIndexToSort).getCellValue();
				return cellValueToSort1.compareTo(cellValueToSort2);
			};

			Comparator<SheetRow> filterComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(columnIndexToFilter)
						.getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(columnIndexToFilter)
						.getCellValue();

				if (tokenToFilter.equals(cellValueToSort1)) {
					cellValueToSort1 = "A_" + cellValueToSort1;
				}
				if (tokenToFilter.equals(cellValueToSort2)) {
					cellValueToSort2 = "A_" + cellValueToSort2;
				}
				return cellValueToSort1.compareTo(cellValueToSort2);
			};

			List<SheetRow> sortedSheetRowList = excelSheet.getSheetRowList().stream()
					.sorted(filterComparator.thenComparing(priorityComparator)).collect(Collectors.toList());

			if (this.didSheetSort(sortedSheetRowList)) {
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				this.sortExcelSheet(sortedSheetRowList, sheetCellTypeList, formulaEvaluator);

				System.out.println("sortSheet - Saving WorkBook.");
				this.writeExcel(myWorkBook, filePath);
			}

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

	public boolean setFilter(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToFilter, String tokenToFilter) throws Exception {
		System.out.println("setFilter. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(filePath);

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			int lastRow = excelSheet.getSheetRowList().size();

			XSSFSheet mySheet = myWorkBook.getSheet(sheetName);

			this.setCriteriaFilter(mySheet, columnIndexToFilter, 1, lastRow,
					tokenToFilter);

			System.out.println("setFilter - Saving WorkBook.");
			this.writeExcel(myWorkBook, filePath);

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

	private void setCriteriaFilter(XSSFSheet mySheet, int cellNumberToFilter, int firstRow, int lastRow,
			String criteria) throws Exception {

		mySheet.setAutoFilter(new CellRangeAddress(0, lastRow, 0, cellNumberToFilter));

		CTAutoFilter ctAutoFilter = mySheet.getCTWorksheet().getAutoFilter();
		CTFilterColumn ctFilterColumn = null;
		for (CTFilterColumn filterColumn : ctAutoFilter.getFilterColumnList()) {
			if (filterColumn.getColId() == cellNumberToFilter)
				ctFilterColumn = filterColumn;
		}
		if (ctFilterColumn == null) {
			ctFilterColumn = ctAutoFilter.addNewFilterColumn();
		}
		ctFilterColumn.setColId(cellNumberToFilter);
		if (ctFilterColumn.isSetFilters()) {
			ctFilterColumn.unsetFilters();
		}

		CTFilters ctFilters = ctFilterColumn.addNewFilters();

		ctFilters.addNewFilter().setVal(criteria);

		/*
		 * for (int i = 1; i <= lastRow; i++) { if
		 * (this.readCell(mySheet.getRow(i).getCell(cellNumberToFilter)).equals(criteria
		 * )) { mySheet.getRow(i).setZeroHeight(false); } else {
		 * mySheet.getRow(i).setZeroHeight(true); } }
		 */

	}

	private void sortExcelSheet(List<SheetRow> sortedSheetRowList, List<SheetCellType> sheetCellTypeList,
			XSSFFormulaEvaluator formulaEvaluator) {
		for (int i = 0; i < sortedSheetRowList.size(); i++) {
			SheetRow sheetRowToMove = sortedSheetRowList.get(i);
			if (sheetRowToMove.getRowNumber() != (i + INITIAL_ROW_NUMBER)) {
				SheetRow sheetRowToReplace = this.findSheetRowToReplace(sortedSheetRowList, (i + INITIAL_ROW_NUMBER));
				
				List<SheetCellType> doNotReplaceSheetCellTypeList = new ArrayList<>();
				doNotReplaceSheetCellTypeList.add(SheetCellType.ID);
				
				List<CellType> doNotReplaceCellTypeList = new ArrayList<>();
				doNotReplaceCellTypeList.add(CellType.FORMULA);
				
				this.replaceSheetCellList(sheetRowToReplace, sheetRowToMove, sheetCellTypeList, 
						formulaEvaluator, doNotReplaceSheetCellTypeList, doNotReplaceCellTypeList);
			}
		}
	}

	private void replaceSheetCellList(SheetRow sheetRowToReplace, SheetRow sheetRowToMove,
			List<SheetCellType> sheetCellTypeList, 
			XSSFFormulaEvaluator formulaEvaluator, 
			List<SheetCellType> doNotReplaceSheetCellTypeList, 
			List<CellType> doNotReplaceCellTypeList) {
		
	
		for (SheetCell sheetCell: sheetRowToReplace.getSheetCellList()) {
			
			if(
				(
					doNotReplaceSheetCellTypeList == null || 
					doNotReplaceSheetCellTypeList.stream().allMatch(rsct -> rsct != sheetCell.getSheetCellType())
				)
				&&
				(
					doNotReplaceCellTypeList == null || 
						doNotReplaceCellTypeList.stream().allMatch(rsct -> rsct != sheetCell.getSheetCellType().getCellType())
				)	
			) {
			
				SheetCell sheetCellToMove = sheetRowToMove.getSheetCellList().get(sheetCell.getSheetCellType().getColumnIndex());

				Cell cellToReplace = sheetCell.getCell();

				cellToReplace.setBlank();

				if (sheetCell.getSheetCellType().isDate()) {
					if (sheetCellToMove.getCellValue() != null) {
						cellToReplace
								.setCellValue(this.getUtilDateService().getLocalDate(sheetCellToMove.getCellValue()));
					}
				} else if (sheetCell.getSheetCellType().getCellType() == CellType.FORMULA) {
					cellToReplace.setCellFormula(sheetCellToMove.getSheetCellType().getSheetFormula().getFormula());
					formulaEvaluator.evaluateFormulaCell(cellToReplace);

				} else {
					cellToReplace.setCellValue(sheetCellToMove.getCellValue());
				}
			}
		}
	}

	private SheetRow findSheetRowToReplace(List<SheetRow> sortedSheetRowList, int rowNumber) {
		return sortedSheetRowList.stream().filter(sr -> sr.getRowNumber() == rowNumber).findFirst().orElse(null);
	}

	private boolean didSheetSort(List<SheetRow> sortedSheetRowList) {
		for (int i = 0; i < sortedSheetRowList.size(); i++) {
			if (sortedSheetRowList.get(i).getRowNumber() != (i + INITIAL_ROW_NUMBER)) {
				return true;
			}
		}
		return false;
	}
	
	private void createSheet(XSSFWorkbook myWorkBook, ExcelSheet excelSheet, int lastRow, List<SheetCellType> sheetCellTypeList) {
		XSSFSheet sheet = myWorkBook.createSheet(excelSheet.getSheetName());
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
		
		myWorkBook.setSheetOrder(excelSheet.getSheetName(), 0);
		myWorkBook.setSelectedTab(0);
		myWorkBook.setActiveSheet(0);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = myWorkBook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		//headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		//headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) myWorkBook).createFont();
		//font.setFontName("Arial");
		//font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		for (SheetCellType sheetCellType: sheetCellTypeList) {
			String headerCellValue = sheetCellType.getName();

			Cell headerCell = header.createCell(sheetCellType.getColumnIndex());
			headerCell.setCellValue(headerCellValue);
			headerCell.setCellStyle(headerStyle);
		}

		for (int i = 1; i <= lastRow; i++) {
			Row row = sheet.createRow(i);
			this.completeRow(row, myWorkBook, sheetCellTypeList);
			
			XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
			

			Iterator<Cell> cellIterator = row.cellIterator();
			List<Cell> cellList = new ArrayList<>();
			// For each row, iterate through each columns
			while (cellIterator.hasNext()) {
				cellList.add(cellIterator.next());
			}
			List<SheetCell> sheetCellList = this.getSheetCellList(cellList,
					sheetCellTypeList, row.getRowNum());
			
			SheetRow sheetRowToReplace = new SheetRow(sheetCellList, row.getRowNum());
			SheetRow sheetRowToMove = excelSheet.getSheetRowList().get(i - 1);
			
			this.replaceSheetCellList(sheetRowToReplace, sheetRowToMove, sheetCellTypeList, 
					formulaEvaluator, null, null);
		}
		
		this.setAutoSizeColumn(sheet, sheetCellTypeList);
	}
	
	private void setAutoSizeColumn(XSSFSheet sheet, List<SheetCellType> sheetCellTypeList) {
		
		for (SheetCellType sheetCellType: sheetCellTypeList) {	
			sheet.setColumnWidth(sheetCellType.getColumnIndex(), sheetCellType.getColumnWidth() * 256);
		}
	}

	private void deleteSheet(XSSFWorkbook myWorkBook, String sheetName) {
		int index = 0;
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
		if (mySheet != null) {
			index = myWorkBook.getSheetIndex(mySheet);
			myWorkBook.removeSheetAt(index);
		}
	}

	protected List<SheetRow> calculateIncompleteSheetRowList(List<SheetRow> sheetRowList) {
		return sheetRowList.stream().filter(sr -> this.incompleteSheetRow(sr)).collect(Collectors.toList());
	}

	private boolean incompleteSheetRow(SheetRow sheetRow) {
		return sheetRow.getSheetCellList().stream().anyMatch(sc -> this.incompleteSheetCell(sc));
	}

	private boolean incompleteSheetCell(SheetCell sheetCell) {
		String strCell = this.readCell(sheetCell.getCell());
		return sheetCell.getSheetCellType().isRequired() && (strCell == null || strCell.trim().equals(""));
	}

	protected List<SheetCell> calculateIncompleteSheetCell(List<SheetCell> incompleteSheetCellList) {
			return incompleteSheetCellList.stream()
					.filter(sc -> this.incompleteSheetCell(sc)).collect(Collectors.toList());
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
					List<SheetCell> sheetCellList = this.getSheetCellList(cellList,
							sheeCellTypeList, row.getRowNum());
					excelSheet.getSheetRowList().add(new SheetRow(sheetCellList, row.getRowNum()));
				}

			} else {
				if (validCellListResponse == ValidCellListResponse.INVALID_HEADER_CELL_LIST) {
					System.out.println("ERROR: INVALID_HEADER_CELL_LIST");
					throw new Exception("INVALID_HEADER_CELL_LIST");
				}
			}

		}

		this.checkCellId(excelSheet.getSheetRowList());
		this.checkDateCell(myWorkBook, excelSheet.getSheetRowList());
		// this.setCellsStyle(myWorkBook, excelSheet);

		return excelSheet;
	}

	private void checkCellId(List<SheetRow> sheetRowList) {
		// System.out.println("checkCellId size: " + sheetRowList.size());
		for (SheetRow sheetRow : sheetRowList) {
			SheetCell sheetCellId = sheetRow.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType() == SheetCellType.ID).findFirst().orElse(null);
			if (!String.valueOf(sheetRow.getRowNumber()).equals(sheetCellId.getCellValue())) {
				// System.out.println("checkCellId - Sheet Row Number: " +
				// sheetRow.getRowNumber() + " / rowNumber: " + sheetCellId.getCellValue());
				String newCellValue = String.valueOf(sheetRow.getRowNumber());
				sheetCellId.getCell().setCellValue(newCellValue);
				sheetCellId.setCellValue(newCellValue);
			}
		}
	}

	private void checkDateCell(XSSFWorkbook myWorkBook, List<SheetRow> sheetRowList) {
		// System.out.println("checkDateCell size: " + sheetRowList);
		for (SheetRow sheetRow : sheetRowList) {
			List<SheetCell> sheetDateCells = sheetRow.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType().isDate()).collect(Collectors.toList());
			for (SheetCell sheetCell : sheetDateCells) {
				if (sheetCell.getSheetCellType().getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(sheetCell.getCell())) {
					// System.out.println("checkDateCell - Sheet Row Number: " +
					// sheetRow.getRowNumber() + " / cellValue: " + sheetCell.getCellValue());
					this.setBlankCellAndCellStyle(myWorkBook, sheetCell.getCell(), true, false, HorizontalAlignment.CENTER);

					if (sheetCell.getSheetCellType().isRequired()) {
						sheetCell.getCell().setCellValue(LocalDate.now());
						sheetCell.setCellValue(this.readCell(sheetCell.getCell()));
					}

				}
			}
		}
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

	private List<SheetCell> getSheetCellList(List<Cell> cellList, List<SheetCellType> sheetCellTypeList,
			int rowNumber) {
		List<SheetCell> sheetCellList = new ArrayList<>();
		for (Cell cell: cellList) {
			
			SheetCellType sheetCellType = sheetCellTypeList.stream().filter(ct -> ct.getColumnIndex() == cell.getColumnIndex()).findFirst().orElse(null);
					
			sheetCellList.add(new SheetCell(
					sheetCellType, 
					this.readCell(cell), 
					cell));
		}
		return sheetCellList;
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

	private ValidCellListResponse validCellList(List<Cell> cellList, int rowNumber, List<SheetCellType> sheeCellTypeList) {
		int requiredCellNumber = sheeCellTypeList.stream().filter(ct -> ct.isRequired()).findFirst().orElse(null).getColumnIndex();
		if (!this.validCell(cellList.get(requiredCellNumber))) {
			return ValidCellListResponse.INVALID_REQUIRED_CELL;
		}
		if (rowNumber == 0 && !this.validHeaderCellList(cellList)) {
			return ValidCellListResponse.INVALID_HEADER_CELL_LIST;
		}
		return ValidCellListResponse.OK;
	}

	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || cell.getCellType() == CellType.NUMERIC
				|| cell.getCellType() == CellType.FORMULA;
	}

	private boolean validHeaderCellList(List<Cell> cellList) {
		List<String> strCellList = this.getStrCellList(cellList);

		return Arrays.stream(SheetCellType.values()).allMatch(sct -> {
			boolean match = strCellList.stream().filter(sc -> sct.getName().equals(sc)).findFirst().isPresent();
			 //System.out.println(sct.getName() + ":" + match);
			return match;
		});

	}

	private List<String> getStrCellList(List<Cell> cellList) {
		List<String> strCellList = new ArrayList<>();

		for (Cell cell : cellList) {
			strCellList.add(this.readCell(cell));
		}
		return strCellList;
	}

	public String readCell(Cell cell) {

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return this.getUtilDateService().getStrDate(cell.getDateCellValue());
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case FORMULA:
			return cell.getRichStringCellValue().toString();
		default:
			return null;
		}
	}

	public UtilDateService getUtilDateService() {
		return utilDateService;
	}

	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}

}
