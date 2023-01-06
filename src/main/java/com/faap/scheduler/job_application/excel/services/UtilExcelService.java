package com.faap.scheduler.job_application.excel.services;

import java.time.Duration;
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

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.Priority;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;

public class UtilExcelService {
	private UtilDateService utilDateService;

	public UtilExcelService(UtilDateService utilDateService) {
		this.setUtilDateService(utilDateService);
	}

	public List<RowWrapper> calculateIncompleteSheetRowList(List<RowWrapper> wrapperRowList) {
		return wrapperRowList.stream().filter(sr -> this.incompleteSheetRow(sr)).collect(Collectors.toList());
	}
	
	private boolean incompleteSheetRow(RowWrapper rowWrapper) {
		//System.out.println("RowNumber: " + rowWrapper.getRowNumber());
		return rowWrapper.getCellWrapperList().stream().anyMatch(sc -> this.incompleteSheetCell(sc));
	}
	
	private boolean incompleteSheetCell(CellWrapper cellWrapper) {
		String strCell = cellWrapper.getCellValue();
		//System.out.println("columnName: " + cellWrapper.getSheetCellType().getName() + " / cellValue: " + cellWrapper.getCellValue() + " / Required: " + cellWrapper.getSheetCellType().isRequired());
		return cellWrapper.getCellTypeWrapper().isRequired() && (strCell == null || strCell.trim().equals(""));
	}
	
	public List<CellWrapper> calculateIncompleteCellWrapper(List<CellWrapper> incompleteSheetCellList) {
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
			try {
				return cell.getRichStringCellValue().toString();
			}
			catch (Exception e) {
				return String.valueOf(cell.getNumericCellValue());
			}
		default:
			return null;
		}
	}
	
	public List<RowWrapper> sortSheetRowList(List<RowWrapper> wrapperRowList, 
			List<Integer> columnIndexToSortList, Comparator<RowWrapper> finalComparator) {
		
		for(int columnIndexToSort: columnIndexToSortList) {
			Comparator<RowWrapper> priorityComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getCellWrapperList().get(columnIndexToSort).getCellValue();
				String cellValueToSort2 = sr2.getCellWrapperList().get(columnIndexToSort).getCellValue();
				
				if(sr1.getCellWrapperList().get(columnIndexToSort).getCellTypeWrapper().isDate()
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
		
		return wrapperRowList.stream()
				.sorted(finalComparator).collect(Collectors.toList());
	
	}
	
	public List<RowWrapper> sortRowWrapperList(List<RowWrapper> wrapperRowList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {

		Comparator<RowWrapper> filterComparator = (sr1, sr2) -> {
			String cellValueToSort1 = sr1.getCellWrapperList().get(columnIndexToFilter)
					.getCellValue();
			String cellValueToSort2 = sr2.getCellWrapperList().get(columnIndexToFilter)
					.getCellValue();

			if (tokenToFilter.equals(cellValueToSort1)) {
				cellValueToSort1 = "A_" + cellValueToSort1;
			}
			if (tokenToFilter.equals(cellValueToSort2)) {
				cellValueToSort2 = "A_" + cellValueToSort2;
			}
			return cellValueToSort1.compareTo(cellValueToSort2);
		};

		return this.sortSheetRowList(wrapperRowList, columnIndexToSortList, filterComparator);
	}
	
	public boolean didSheetSort(List<RowWrapper> sortedSheetRowList) {
		int rowNumber = 0; //Header
		for (RowWrapper rowWrapper: sortedSheetRowList) {
			rowNumber++;
			if (rowWrapper.getRowNumber() != rowNumber) {
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
	
	public void completeRow(Row row, XSSFWorkbook myWorkBook, List<CellTypeWrapper> sheeCellTypeList) {
		List<Cell> cellList = new ArrayList<>();
		Iterator<Cell> cellIterator = row.cellIterator();
		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(cell.getColumnIndex() < sheeCellTypeList.size()) {
				cellList.add(cell);
			}
		}

		for (CellTypeWrapper cellTypeWrapper: sheeCellTypeList) {
			if (this.existColumnIndex(cellList, cellTypeWrapper.getColumnIndex())) {
				CellType cellType = cellTypeWrapper.getCellType();
				Cell cell = row.createCell(cellTypeWrapper.getColumnIndex(), cellType);
				
				HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
				if(cellTypeWrapper.isRequired()) {
					horizontalAlignment = HorizontalAlignment.LEFT;
				}
				
				boolean isText = cell.getCellType() == CellType.STRING;
				
				this.setBlankCellAndCellStyle(myWorkBook, cell, cellTypeWrapper.isDate(), isText, horizontalAlignment);
			}
		}
	}
	
	public List<Cell> getCellList(Row row, List<CellTypeWrapper> wrapperCellTypeList) {
		Iterator<Cell> cellIterator = row.cellIterator();

		List<Cell> cellList = new ArrayList<>();

		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if(cell.getColumnIndex() < wrapperCellTypeList.size()) {
				cellList.add(cell);
			}
		}
		
		return cellList;
		
	}
	
    public Priority getPriority(LocalDate estimatedDate) {
    	long diffDays = Duration.between(LocalDate.now().atStartOfDay(), estimatedDate.atStartOfDay()).toDays();
    	for(Priority p: Priority.values()) {
    		if(diffDays < p.getDayNumberToEstimatedDate()) {
    			return p;
    		}
    	}
    	return Priority.A1;
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
	
	public List<RowWrapper> getRowWrapperList(List<ThingToDo> thingToDoList, List<CellTypeWrapper> cellTypeWrapperList, int lastRowNumber) {
		List<RowWrapper> rowWrapperList = new ArrayList<>();
		
		for(ThingToDo ttd: thingToDoList) {
			List<CellWrapper> cellWrapperList = new ArrayList<>();
			
			CellTypeWrapper cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.ID.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					ttd.getToken(), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.INCIDENCE_DATE.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					(ttd.getIncidenceDate() != null ? this.utilDateService.getStrDate(ttd.getIncidenceDate()) : null), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.EXECUTION_DATE.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					(ttd.getExecutionDate() != null ? this.utilDateService.getStrDate(ttd.getExecutionDate()) : null), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.ESTIMATED_DATE.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					(ttd.getEstimatedDate() != null ? this.utilDateService.getStrDate(ttd.getEstimatedDate()) : null), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.PRIORITY.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					String.valueOf(ttd.getPriority()), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.THINGS_TO_DO.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					String.valueOf(ttd.getThingToDo()), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.CATEGORY.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					String.valueOf(ttd.getCategory()), 
					null));
			
			cellTypeWrapper = cellTypeWrapperList.stream().filter(ct -> ct.getColumnIndex() == ThingToDoColumnType.STATUS.getColumnIndex()).findFirst().orElse(null);
			
			cellWrapperList.add(new CellWrapper(
					cellTypeWrapper, 
					String.valueOf(ttd.getStatus()), 
					null));
			
			RowWrapper rowWrapper = new RowWrapper(cellWrapperList, ++lastRowNumber);
			
			rowWrapperList.add(rowWrapper);
		}
		
		return rowWrapperList;
	}
	
}
