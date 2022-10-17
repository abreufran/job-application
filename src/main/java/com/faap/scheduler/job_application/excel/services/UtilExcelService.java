package com.faap.scheduler.job_application.excel.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetCell;
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
	
	public List<SheetRow> sortSheetRowList(List<SheetRow> sheetRowList, int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {
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

		return sheetRowList.stream()
				.sorted(filterComparator.thenComparing(priorityComparator)).collect(Collectors.toList());
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
}
