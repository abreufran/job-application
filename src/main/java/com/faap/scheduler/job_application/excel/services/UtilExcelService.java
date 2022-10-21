package com.faap.scheduler.job_application.excel.services;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class UtilExcelService {
	private UtilDateService utilDateService;

	public UtilExcelService(UtilDateService utilDateService) {
		this.setUtilDateService(utilDateService);
	}

	public List<SheetRow> calculateIncompleteSheetRowList(List<SheetRow> sheetRowList) {
		return sheetRowList.stream().filter(sr -> this.incompleteSheetRow(sr)).collect(Collectors.toList());
	}
	
	private boolean incompleteSheetRow(SheetRow sheetRow) {
		//System.out.println("RowNumber: " + sheetRow.getRowNumber());
		return sheetRow.getSheetCellList().stream().anyMatch(sc -> this.incompleteSheetCell(sc));
	}
	
	private boolean incompleteSheetCell(SheetCell sheetCell) {
		String strCell = sheetCell.getCellValue();
		//System.out.println("columnName: " + sheetCell.getSheetCellType().getName() + " / cellValue: " + sheetCell.getCellValue() + " / Required: " + sheetCell.getSheetCellType().isRequired());
		return sheetCell.getSheetCellType().isRequired() && (strCell == null || strCell.trim().equals(""));
	}
	
	public List<SheetCell> calculateIncompleteSheetCell(List<SheetCell> incompleteSheetCellList) {
		return incompleteSheetCellList.stream()
				.filter(sc -> this.incompleteSheetCell(sc)).collect(Collectors.toList());
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
	
	public List<SheetRow> sortSheetRowList(List<SheetRow> sheetRowList, 
			List<Integer> columnIndexToSortList, Comparator<SheetRow> finalComparator) {
		
		for(int columnIndexToSort: columnIndexToSortList) {
			Comparator<SheetRow> priorityComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(columnIndexToSort).getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(columnIndexToSort).getCellValue();
				
				if(sr1.getSheetCellList().get(columnIndexToSort).getSheetCellType().isDate()
						&& cellValueToSort1 != null && cellValueToSort2 != null) {
					LocalDate cellDateToSort1 = this.utilDateService.getLocalDate(cellValueToSort1);
					LocalDate cellDateToSort2 = this.utilDateService.getLocalDate(cellValueToSort2);
					
					return cellDateToSort2.compareTo(cellDateToSort1);
				}
				
				if(cellValueToSort1 == null) cellValueToSort1 = "Z";
				if(cellValueToSort2 == null) cellValueToSort2 = "Z";
				
				return cellValueToSort1.compareTo(cellValueToSort2);
			};
			
			if(finalComparator == null) {
				finalComparator = priorityComparator;
			}
			else {
				finalComparator = finalComparator.thenComparing(priorityComparator);
			}
			
		}	
		
		return sheetRowList.stream()
				.sorted(finalComparator).collect(Collectors.toList());
	
	}
	
	public List<SheetRow> sortSheetRowList(List<SheetRow> sheetRowList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {

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

		return this.sortSheetRowList(sheetRowList, columnIndexToSortList, filterComparator);
	}
	
	public boolean didSheetSort(List<SheetRow> sortedSheetRowList) {
		int rowNumber = 0; //Header
		for (SheetRow sheetRow: sortedSheetRowList) {
			rowNumber++;
			if (sheetRow.getRowNumber() != rowNumber) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existSheet(XSSFWorkbook myWorkBook, String sheetName) {
		return myWorkBook.getSheet(sheetName) != null;
	}
	
	public UtilDateService getUtilDateService() {
		return utilDateService;
	}
	
	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}
	
	public void completeRow(Row row, XSSFWorkbook myWorkBook, List<SheetCellType> sheeCellTypeList) {
		List<Cell> cellList = new ArrayList<>();
		Iterator<Cell> cellIterator = row.cellIterator();
		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(cell.getColumnIndex() < sheeCellTypeList.size()) {
				cellList.add(cell);
			}
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
	
	public List<Cell> getCellList(Row row, List<SheetCellType> sheetCellTypeList) {
		Iterator<Cell> cellIterator = row.cellIterator();

		List<Cell> cellList = new ArrayList<>();

		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(cell.getColumnIndex() < sheetCellTypeList.size()) {
				cellList.add(cell);
			}
		}
		
		return cellList;
		
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
}
