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
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters;

import com.faap.scheduler.job_application.excel.dtos.ExcelRequest;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.models.SheetType;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.job.ValidCellListResponse;

public abstract class AbstractExcelService {
	public static int INITIAL_ROW_NUMBER = 2;
	private UtilDateService utilDateService;
	
	public AbstractExcelService(UtilDateService utilDateService) {
		this.setUtilDateService(utilDateService);
	}
	
	public abstract void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook);

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
	
	public boolean setFilter(ExcelRequest excelRequest) throws Exception {
		System.out.println("setFilter. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(excelRequest.getFilePath());
			
			ExcelSheet excelSheet = this.readSheet(myWorkBook, excelRequest.getSheetType(), 
					excelRequest.getNumberOfCells(), excelRequest.getRequiredCellNumber());
			
			int lastRow = excelSheet.getSheetRowList().size();
		
			XSSFSheet mySheet = myWorkBook.getSheetAt(excelRequest.getSheetType().getSheetNumber());
			
			this.setCriteriaFilter(mySheet, excelRequest.getCellNumberToFilter(), 1, lastRow, excelRequest.getTokenToFilter());
			
			System.out.println("setFilter - Saving WorkBook.");
			this.writeExcel(myWorkBook, excelRequest.getFilePath());
		
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if(myWorkBook != null) {
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
		for (int i = 1; i <= lastRow; i++) {
			if (this.readCell(mySheet.getRow(i).getCell(cellNumberToFilter)).equals(criteria)) {
				mySheet.getRow(i).setZeroHeight(false);
			} else {
				mySheet.getRow(i).setZeroHeight(true);
			}
		}*/

	}
	
	public boolean fillEmptyFields(ExcelRequest excelRequest) {
		System.out.println("Fill empty fields. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(excelRequest.getFilePath());
			
			ExcelSheet excelSheet = this.readSheet(myWorkBook, excelRequest.getSheetType(), 
					excelRequest.getNumberOfCells(), excelRequest.getRequiredCellNumber());
			
			List<SheetRow> incompleteSheetRowList = this.calculateIncompleteSheetRowList(excelSheet.getSheetRowList());
			
			List<SheetCell> incompleteSheetCellList = this.calculateIncompleteSheetCell(incompleteSheetRowList);
			
			this.completeSheetCellList(incompleteSheetCellList, myWorkBook);
			
			if(incompleteSheetCellList.size() > 0) {
					
				System.out.println("fillEmptyFields - Saving WorkBook.");
				this.writeExcel(myWorkBook, excelRequest.getFilePath());
				
			}
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if(myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean sortSheet(ExcelRequest excelRequest) {
		System.out.println("Sort Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(excelRequest.getFilePath());
			
			ExcelSheet excelSheet = this.readSheet(myWorkBook, excelRequest.getSheetType(), 
					excelRequest.getNumberOfCells(), excelRequest.getRequiredCellNumber());
			
			Comparator<SheetRow> priorityComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(excelRequest.getCellNumberToSort()).getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(excelRequest.getCellNumberToSort()).getCellValue();
				return cellValueToSort1.compareTo(cellValueToSort2);
			};
			
			Comparator<SheetRow> filterComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(excelRequest.getCellNumberToFilter()).getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(excelRequest.getCellNumberToFilter()).getCellValue();
				
				if(excelRequest.getTokenToFilter().equals(cellValueToSort1)) {
					cellValueToSort1 = "A_" + cellValueToSort1;
				}
				if(excelRequest.getTokenToFilter().equals(cellValueToSort2)) {
					cellValueToSort2 = "A_" + cellValueToSort2;
				}
				return cellValueToSort1.compareTo(cellValueToSort2);
			};
			
			List<SheetRow> sortedSheetRowList = excelSheet.getSheetRowList()
				.stream()
				.sorted(filterComparator.thenComparing(priorityComparator))
				.collect(Collectors.toList());

			if(this.didSheetSort(sortedSheetRowList)) {
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				this.sortExcelSheet(sortedSheetRowList, excelSheet.getSheetCellTypeHashMap(), formulaEvaluator);
				
				System.out.println("sortSheet - Saving WorkBook.");
				this.writeExcel(myWorkBook, excelRequest.getFilePath());
			}
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if(myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sortExcelSheet(List<SheetRow> sortedSheetRowList, Map<Integer,SheetCellType> sheetCellTypeHashMap,
			XSSFFormulaEvaluator formulaEvaluator) {
		for(int i = 0; i < sortedSheetRowList.size(); i++) {
			SheetRow sheetRowToMove = sortedSheetRowList.get(i);
			if(sheetRowToMove.getRowNumber() != (i + INITIAL_ROW_NUMBER)) {
				SheetRow sheetRowToReplace = this.findSheetRowToReplace(sortedSheetRowList, (i + INITIAL_ROW_NUMBER));
				this.replaceSheetCellList(sheetRowToReplace, sheetRowToMove, sheetCellTypeHashMap, formulaEvaluator);
			}
		}
	}
	
	private void replaceSheetCellList(SheetRow sheetRowToReplace, SheetRow sheetRowToMove, 
			Map<Integer,SheetCellType> sheetCellTypeHashMap, XSSFFormulaEvaluator formulaEvaluator) {
		for(int i = 0; i < sheetRowToReplace.getSheetCellList().size(); i++) {
			SheetCell sheetCellToReplace = sheetRowToReplace.getSheetCellList().get(i);
			if(sheetCellToReplace.getSheetCellType() != SheetCellType.ID 
					&& sheetCellToReplace.getSheetCellType().getCellType() != CellType.FORMULA) {
				SheetCell sheetCellToMove = sheetRowToMove.getSheetCellList().get(i);
				
				Cell cellToReplace = sheetCellToReplace.getCell();
				
				cellToReplace.setBlank();
							
				if(sheetCellTypeHashMap.get(i).isDate()) {
					if(sheetCellToMove.getCellValue() != null) {
						cellToReplace.setCellValue(this.getUtilDateService().getLocalDate(sheetCellToMove.getCellValue()));
					}	
				}
				else if(sheetCellTypeHashMap.get(i).getCellType() == CellType.FORMULA) {
					cellToReplace.setCellFormula(sheetCellToMove.getCellFormula());
					formulaEvaluator.evaluateFormulaCell(cellToReplace);
		
				}
				else {
					cellToReplace.setCellValue(sheetCellToMove.getCellValue());
				}
			}
		}
	}
	
	private SheetRow findSheetRowToReplace(List<SheetRow> sortedSheetRowList, int rowNumber) {
		return sortedSheetRowList.stream().filter(sr -> sr.getRowNumber() == rowNumber).findFirst().orElse(null);
	}
	
	
	private boolean didSheetSort(List<SheetRow> sortedSheetRowList) {
		for(int i = 0; i < sortedSheetRowList.size(); i++) {
			if(sortedSheetRowList.get(i).getRowNumber() != (i + INITIAL_ROW_NUMBER)) {
				return true;
			}
		}
		return false;
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
	
	private List<SheetCell> calculateIncompleteSheetCell(List<SheetRow> incompleteSheetRowList) {
		List<SheetCell> incompleteSheetCellList = new ArrayList<>();
		for(SheetRow sheetRow: incompleteSheetRowList) {
			List<SheetCell> sheetCellList = sheetRow.getSheetCellList().stream().filter(sc -> this.incompleteSheetCell(sc)).collect(Collectors.toList());
			incompleteSheetCellList.addAll(sheetCellList);
		}
		return incompleteSheetCellList;
	}
	
	public ExcelSheet readSheet(XSSFWorkbook myWorkBook, 
			SheetType sheetType, 
			int numberOfCells, 
			int requiredCellNumber) throws Exception {
		
		ExcelSheet excelSheet = new ExcelSheet(sheetType);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(sheetType.getSheetNumber());

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		
		int rowNumber = 0;
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			this.completeRow(row, myWorkBook, numberOfCells, excelSheet.getSheetCellTypeHashMap());
			rowNumber++;
			Iterator<Cell> cellIterator = row.cellIterator();
			
			List<Cell> cellList = new ArrayList<>();
			
			// For each row, iterate through each columns
			while (cellIterator.hasNext()) {	
				cellList.add(cellIterator.next());
			}
			
			ValidCellListResponse validCellListResponse = this.validCellList(cellList, rowNumber, numberOfCells, requiredCellNumber);
			if(validCellListResponse == ValidCellListResponse.INVALID_CELLS_NUMBER) {
				
			}
			
			if(validCellListResponse == ValidCellListResponse.OK) {
				if(rowNumber == 1) {
					for(int i = 0; i < cellList.size(); i++) {
						excelSheet.getSheetCellTypeHashMap().put(i, this.getSheetCellType(cellList.get(i)));
					}
				}
				else {
					List<SheetCell> sheetCellList = this.getSheetCellList(cellList, excelSheet.getSheetCellTypeHashMap(), rowNumber);
					excelSheet.getSheetRowList().add(new SheetRow(sheetCellList, rowNumber));
				}
				
			}
			else {
				if(validCellListResponse == ValidCellListResponse.INVALID_CELLS_NUMBER) {
					List<String> strCellList = this.getStrCellList(cellList);
					System.out.print("ROW: " + rowNumber + " ERROR: \t" + strCellList.size() + "\t");
					strCellList.stream().forEach(c -> System.out.print(c + "\t"));
					System.out.println("");
					throw new Exception("INVALID_CELLS_NUMBER");
				}
				if(validCellListResponse == ValidCellListResponse.INVALID_HEADER_CELL_LIST) {
					System.out.print("ERROR: INVALID_HEADER_CELL_LIST");
					throw new Exception("INVALID_HEADER_CELL_LIST");
				}
			}

			
		}
		
		this.checkCellId(excelSheet.getSheetRowList());
		this.checkDateCell(myWorkBook, excelSheet.getSheetRowList());
		//this.setCellsStyle(myWorkBook, excelSheet);
		
		return excelSheet;
	}
	
	private void checkCellId(List<SheetRow> sheetRowList) {
		//System.out.println("checkCellId size: " + sheetRowList.size());
		for(SheetRow sheetRow: sheetRowList) {
			SheetCell sheetCellId = sheetRow.getSheetCellList().stream().filter(sc -> sc.getSheetCellType() == SheetCellType.ID).findFirst().orElse(null);
			if(!String.valueOf(sheetRow.getRowNumber()).equals(sheetCellId.getCellValue())) {
				//System.out.println("checkCellId - Sheet Row Number: " + sheetRow.getRowNumber() + " / rowNumber: " + sheetCellId.getCellValue());
				String newCellValue = String.valueOf(sheetCellId.getRowNumber());
				sheetCellId.getCell().setCellValue(newCellValue);
				sheetCellId.setCellValue(newCellValue);
			}
		}
	}
	
	private void checkDateCell(XSSFWorkbook myWorkBook, List<SheetRow> sheetRowList) {
		//System.out.println("checkDateCell size: " + sheetRowList);
		for(SheetRow sheetRow: sheetRowList) {
			List<SheetCell> sheetDateCells = sheetRow.getSheetCellList().stream().filter(sc -> sc.getSheetCellType().isDate()).collect(Collectors.toList());
			for(SheetCell sheetCell: sheetDateCells) {
				if (sheetCell.getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(sheetCell.getCell())) {
					//System.out.println("checkDateCell - Sheet Row Number: " + sheetRow.getRowNumber() + " / cellValue: " + sheetCell.getCellValue());
					this.setBlankCellAndCellStyle(myWorkBook, sheetCell.getCell(), true);
					
					if(sheetCell.getSheetCellType().isRequired()) {
						sheetCell.getCell().setCellValue(LocalDate.now());
						sheetCell.setCellValue(this.readCell(sheetCell.getCell()));
					}
					
					
				}
			}
		}
	}
	
	
	private void setBlankCellAndCellStyle(XSSFWorkbook myWorkBook, Cell cell, boolean isDate) {
		cell.setBlank();
		CellStyle style = myWorkBook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		
		if(isDate) {
			CreationHelper createHelper = myWorkBook.getCreationHelper();
			style.setDataFormat(createHelper.createDataFormat().getFormat(UtilDateService.WRITE_EXCEL_DATE_FORMAT));
		}
		cell.setCellStyle(style);
	}
	
	private SheetCellType getSheetCellType(Cell cell) {
		return Arrays.stream(SheetCellType.values()).filter(sct -> sct.getName().equals(this.readCell(cell))).findFirst().orElse(null);
	}
	
	private List<SheetCell> getSheetCellList(List<Cell> cellList, Map<Integer,SheetCellType> sheetCellTypeHashMap, int rowNumber) {
		List<SheetCell> sheetCellList = new ArrayList<>();
		for(int i = 0; i < cellList.size(); i++) {
			sheetCellList.add(new SheetCell(sheetCellTypeHashMap.get(i), cellList.get(i), rowNumber, this.readCell(cellList.get(i))));
		}
		return sheetCellList;
	}
	
	private void completeRow(Row row, XSSFWorkbook myWorkBook, int numberOfCells, Map<Integer,SheetCellType> sheetCellTypeHashMap) {
		List<Cell> cellList = new ArrayList<>();
		Iterator<Cell> cellIterator = row.cellIterator();
		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {	
			cellList.add(cellIterator.next());
		}
		
		for(int i = 0; i < numberOfCells; i++) {
			if(this.existColumnIndex(cellList, i)) {
				CellType cellType = sheetCellTypeHashMap.get(i).getCellType();
				Cell cell = row.createCell(i, cellType);
				this.setBlankCellAndCellStyle(myWorkBook, cell, sheetCellTypeHashMap.get(i).isDate());
			}
		}
	}
	
	private boolean existColumnIndex(List<Cell> cellList, int columNumber) {
		return !cellList.stream().anyMatch(c -> c.getColumnIndex() == columNumber);
	}
	
	private ValidCellListResponse validCellList(List<Cell> cellList, int rowNumber, int numberOfCells, int requiredCellNumber) {
		if(cellList.size() == 0) {
			return ValidCellListResponse.EMPTY_CELL_LIST;
		}
		if (cellList.size() != numberOfCells) {
			return ValidCellListResponse.INVALID_CELLS_NUMBER;
		}
		if(!this.validCell(cellList.get(requiredCellNumber))) {
			return ValidCellListResponse.INVALID_REQUIRED_CELL;
		}
		if(rowNumber == 1 && !this.validHeaderCellList(cellList)) {
			return ValidCellListResponse.INVALID_HEADER_CELL_LIST;
		}
		return ValidCellListResponse.OK;
	}
	
	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || 
				cell.getCellType() == CellType.NUMERIC ||
				cell.getCellType() == CellType.FORMULA;
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
		
		for(Cell cell: cellList) {
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
	
//	public boolean sortSheet2(XSSFSheet sheet, int column, int rowStart, int lastRow) {
//		boolean sorted = false;
//		boolean sorting = true;
//		
//		//List<Integer> integerRowListSorted = this.getIntegerRowListSorted(sheet);
//		while (sorting == true) {
//			int finalRowToSort = this.getFinalRowToSort(sheet, column, rowStart, lastRow);
//			if(finalRowToSort != -1) {
//			
//				int initilRowToSort = finalRowToSort - 1;
//				
//				sheet.shiftRows(initilRowToSort, initilRowToSort, lastRow - initilRowToSort + 1);
//				sheet.shiftRows(finalRowToSort, finalRowToSort, -1);
//				sheet.shiftRows(lastRow + 1, lastRow + 1, (lastRow - initilRowToSort) * -1);
//				sorted = true;
//			}
//			else {
//				sorting = false;
//			}
//		}
//		
//		return sorted;
//	}
//	
//	private int getFinalRowToSort(XSSFSheet sheet, int column, int rowStart, int lastRow) {
//		List<Row> rowList = new ArrayList<>();
//		for (Row row : sheet) {
//			//if(row.getCell(7).getRichStringCellValue().toString().equals("PENDING")) {
//				rowList.add(row);
//			//}
//		}
//		
//		for (Row row : rowList) {
//            // skip if this row is before first to sort
//            if (row.getRowNum()<rowStart) continue;
//            // end if this is last row
//            if (lastRow==row.getRowNum()) break;
//            Row row2 = sheet.getRow(row.getRowNum()+1);
//            if (row2 == null) continue;
//            String firstValue = (row.getCell(column) != null) ? row.getCell(column).getStringCellValue() : "";
//            String secondValue = (row2.getCell(column) != null) ? row2.getCell(column).getStringCellValue() : "";
//            //compare cell from current row and next row - and switch if secondValue should be before first
//            if (secondValue.compareToIgnoreCase(firstValue)<0) {
//            	return row.getRowNum()+1;
//            }
//		}
//		return -1;
//	}
}
