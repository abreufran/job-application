package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.Periodicity;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.models.Weekday;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public class JobExcelService extends AbstractApiExcelService {
	private static String THINGS_TO_DO_SHEET_NAME = "Things to do";
	private static String PERIODIC_TASKS_SHEET_NAME = "Periodic Tasks";
	public static List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";

	public JobExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		super(utilDateService, utilExcelService, 
				excelReadService,excelWriteService);
	}
	
	public void loadAndSortThingsToDoSheet(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		List<CellTypeWrapper> initialSheetCellTypeList, List<CellTypeWrapper> finalSheetCellTypeList) {
		System.out.println("Load Periodic Task.");
		WorkbookResponse workbookResponse = null;
		XSSFWorkbook myWorkBook = null;
		try {
			workbookResponse = this.fillSortSheet(initialFilePath, finalFilePath, 
					THINGS_TO_DO_SHEET_NAME, finalSheetCellTypeList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
			
			if(workbookResponse.isSuccess()) {
				myWorkBook = workbookResponse.getMyWorkBook();
				
				SheetWrapper initialExcelSheet = this.readSheet(myWorkBook, PERIODIC_TASKS_SHEET_NAME,
						initialSheetCellTypeList);
				
				SheetWrapper finalExcelSheet = this.readSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME,
						finalSheetCellTypeList);
				
				
				for(RowWrapper initialSheetRow: initialExcelSheet.getSheetRowList()) {
					String initialDate = null;
					String priority = null;
					String thingsToDo = null;
					String category = null;
					String periodicity = null;
					String weekday = null;
					
					for(CellWrapper initialSheetCell: initialSheetRow.getSheetCellList()) {
						PeriodicTaskColumnType initialColumnType = Arrays.asList(PeriodicTaskColumnType.values()).stream().filter(ptct -> ptct.getName().equals(initialSheetCell.getSheetCellType().getName())).findFirst().orElse(null);
						switch (initialColumnType) {
						case INITIAL_DATE:
							initialDate = initialSheetCell.getCellValue();
							break;
						case TASK:
							thingsToDo = initialSheetCell.getCellValue();
							break;
						case PRIORITY:
							priority = initialSheetCell.getCellValue();
							break;
						case CATEGORY: 
							category = initialSheetCell.getCellValue();
							break;
						case PERIODICITY: 
							periodicity = initialSheetCell.getCellValue();
							break;
						case WEEKDAY: 
							weekday = initialSheetCell.getCellValue();
							break;	
						default:
							break;
						}
					}
					
					LocalDate estimatedDate = this.getEstimatedDate(finalExcelSheet.getSheetRowList(), thingsToDo, periodicity, weekday, initialDate);
					
					if(!this.existSheetCell(finalExcelSheet.getSheetRowList(), thingsToDo, estimatedDate)) {
						priority = this.utilExcelService.getPriority(estimatedDate).toString();
						
						int lastRowNumber = finalExcelSheet.getSheetRowList().get(finalExcelSheet.getSheetRowList().size() - 1).getRowNumber();
						Row row = this.createBodyRow(myWorkBook, myWorkBook.getSheet(THINGS_TO_DO_SHEET_NAME), finalSheetCellTypeList, lastRowNumber + 1);
						
						List<Cell> cellList = this.utilExcelService.getCellList(row, finalSheetCellTypeList);
						
						RowWrapper rowWrapper = this.excelReadService.addSheetRow(finalExcelSheet, cellList, finalSheetCellTypeList, row);
						
						for(CellWrapper cellWrapper: rowWrapper.getSheetCellList()) {
							this.completeSheetCell(cellWrapper, row.getRowNum(), this.utilDateService.getStrDate(estimatedDate), priority, thingsToDo, category);
						}
					}
					
				}
				
				this.deleteSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME);
				this.addSheetToExcel(myWorkBook, THINGS_TO_DO_SHEET_NAME, 0, finalSheetCellTypeList, finalExcelSheet.getSheetRowList());
				this.sortSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME, finalSheetCellTypeList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
				
				System.out.println("loadPeriodicTasks - Saving WorkBook.");
				this.writeExcel(myWorkBook, finalFilePath);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		
	}

	@Override
	public void completeSheetCellList(List<CellWrapper> wrapperCellList, XSSFWorkbook myWorkBook, int rowNumber) {
		List<CellWrapper> incompleteSheetCellList = this.utilExcelService.calculateIncompleteSheetCell(wrapperCellList);
		
		for(CellWrapper cellWrapper: incompleteSheetCellList) {
			this.completeSheetCell(cellWrapper, rowNumber, null, null, null, null);
		}
		
	}
	
	private void completeSheetCell(
			CellWrapper cellWrapper, 
			int rowNumber, 
			String estimatedDate, 
			String priority, 
			String thingsToDo, 
			String category) {
		
		ThingToDoColumnType thingToDoColumnType = this.getThingToDoColumnType(cellWrapper);
		switch (thingToDoColumnType) {
		case ID:
			cellWrapper.setCellValue(String.valueOf(rowNumber + 1));
			System.out.println("Completing Row: " + (rowNumber + 1) 
					+ " / columnName: " + cellWrapper.getSheetCellType().getName() + " / Value: " + cellWrapper.getCellValue());
			break;
		case INCIDENCE_DATE:
			cellWrapper.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
			break;
		case ESTIMATED_DATE:
			if(estimatedDate == null) {
				cellWrapper.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
			}
			else {
				cellWrapper.setCellValue(estimatedDate);
			}
			break;	
		case PRIORITY:
			if(priority == null) {
				cellWrapper.setCellValue(cellWrapper.getSheetCellType().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(priority);
			}
			break;
		case THINGS_TO_DO:
			if(thingsToDo == null) {
				cellWrapper.setCellValue(cellWrapper.getSheetCellType().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(thingsToDo);
			}
			break;	
		case CATEGORY:
			if(category == null) {
				cellWrapper.setCellValue(cellWrapper.getSheetCellType().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(category);
			}
			break;	
		case STATUS:
			cellWrapper.setCellValue(cellWrapper.getSheetCellType().getDefaultValue().toString());
			break;
		default:
			break;
		}
		System.out.println("Completing Row: " + (rowNumber + 1) 
				+ " / columnName: " + cellWrapper.getSheetCellType().getName() + " / Fixed Value: " + cellWrapper.getCellValue());
	}
	
	@Override
	public void updateCellIdValue(List<RowWrapper> wrapperRowList) {
		for (RowWrapper rowWrapper : wrapperRowList) {
			CellWrapper wrapperCellId = rowWrapper.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType().getName().equals(ThingToDoColumnType.ID.getName())).findFirst().orElse(null);
			
			if (!String.valueOf(rowWrapper.getRowNumber() + 1).equals(wrapperCellId.getCellValue())) {
				wrapperCellId.setCellValue(String.valueOf(rowWrapper.getRowNumber() + 1));
			}
		}
	}
	
	@Override
	public void updatePriority(String sheetName, List<RowWrapper> wrapperRowList) {
		if(THINGS_TO_DO_SHEET_NAME.equals(sheetName)) {
			for (RowWrapper rowWrapper : wrapperRowList) {
				CellWrapper wrapperCellPriority = rowWrapper.getSheetCellList().stream()
						.filter(sc -> sc.getSheetCellType().getName().equals(ThingToDoColumnType.PRIORITY.getName())).findFirst().orElse(null);
				
				CellWrapper wrapperCellEstimatedDate = rowWrapper.getSheetCellList().stream()
						.filter(sc -> sc.getSheetCellType().getName().equals(ThingToDoColumnType.ESTIMATED_DATE.getName())).findFirst().orElse(null);
				
				if (wrapperCellEstimatedDate.getCellValue() != null) {
					wrapperCellPriority.setCellValue(this.utilExcelService.getPriority(this.utilDateService.getLocalDate(wrapperCellEstimatedDate.getCellValue())).toString());
				}
			}
		}
	}
	
	private boolean existSheetCell(List<RowWrapper> wrapperRowList, String thingsToDo, 
			LocalDate estimatedDate) {
		
		return this.findFirstSheetRow(wrapperRowList, thingsToDo, this.utilDateService.getStrDate(estimatedDate)) != null;
	}
	
	private RowWrapper findFirstSheetRow(List<RowWrapper> wrapperRowList, String thingsToDo, String estimatedDate) {
		return wrapperRowList.stream().filter(sr -> {
			boolean thingsToDoMatched = false;
			boolean incidenceDateMatched = false;
			for(CellWrapper cellWrapper: sr.getSheetCellList()) {
				ThingToDoColumnType thingToDoColumnType = this.getThingToDoColumnType(cellWrapper);
				if(thingsToDo == null) {
					thingsToDoMatched = true;
				}
				else if(thingToDoColumnType == ThingToDoColumnType.THINGS_TO_DO && thingsToDo.equals(cellWrapper.getCellValue())) {
					thingsToDoMatched = true;
				}
				
				if(estimatedDate == null) {
					incidenceDateMatched = true;
				}
				else if(thingToDoColumnType == ThingToDoColumnType.ESTIMATED_DATE && estimatedDate.equals(cellWrapper.getCellValue())) {
					incidenceDateMatched = true;
				}
			}
			if(thingsToDoMatched && incidenceDateMatched) {
				return true;
			}
			else {
				return false;
			}
		}).findFirst().orElse(null);
	}
	
	private ThingToDoColumnType getThingToDoColumnType(CellWrapper cellWrapper) {
		return Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(cellWrapper.getSheetCellType().getName())).findFirst().orElse(null);
	}
	
	private LocalDate getEstimatedDate(List<RowWrapper> wrapperRowList, String thingsToDo, 
			String periodicity, String weekday, 
    		String initialDate) {
		LocalDate initialDateOfPeriodicTask = this.utilDateService.getLocalDate(initialDate);
		Periodicity periodicityEnum = Periodicity.getPeriodicity(periodicity);
		Weekday weekdayEnum = Weekday.getWeekday(weekday);
		
		List<RowWrapper> sortedSheetRowList = this.utilExcelService.sortSheetRowList(wrapperRowList, 
				Arrays.asList(ThingToDoColumnType.ESTIMATED_DATE.getColumnIndex()), COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
		
		RowWrapper lastSheetRowWithEstimatedDate = this.findFirstSheetRow(sortedSheetRowList, thingsToDo, null);
		
		LocalDate lastEstimatedDate = null;
		
		if(lastSheetRowWithEstimatedDate != null) {
			CellWrapper estimatedDateSheetCell = lastSheetRowWithEstimatedDate.getSheetCellList()
					.stream()
					.filter(sc -> this.getThingToDoColumnType(sc) == ThingToDoColumnType.ESTIMATED_DATE)
					.findFirst()
					.orElse(null);
			if(estimatedDateSheetCell.getCellValue() != null) {
				lastEstimatedDate = this.utilDateService.getLocalDate(estimatedDateSheetCell.getCellValue());
			}
		}
		
		return this.getEstimatedDate(periodicityEnum, weekdayEnum, 
				initialDateOfPeriodicTask, LocalDate.now(), lastEstimatedDate);
	}
	
	private LocalDate getEstimatedDate(Periodicity periodicity, Weekday weekday, 
    		LocalDate initialDateOfPeriodicTask, LocalDate incidenceDate,
    		LocalDate lastEstimatedDay) {
    	
    	DayOfWeek dayOfWeek = incidenceDate.getDayOfWeek();
    	
    	LocalDate estimatedDate = (weekday.getValue() != -1 
    			? incidenceDate.plusDays(weekday.getValue() - dayOfWeek.getValue()) 
    			: incidenceDate);
    	
    	if(periodicity.getSize() == -1) {
    		switch (periodicity) {
    		case LAST_DAY_MONTH:
    			return incidenceDate.withDayOfMonth(
    										incidenceDate.getMonth().length(incidenceDate.isLeapYear()));
    		case SECOND_SATURDAY_NOVEMBER:
    			LocalDate novemberFirst = LocalDate.of(incidenceDate.getYear(), 11, 1);
    			DayOfWeek dayOfWeekNovemberFirst = novemberFirst.getDayOfWeek();
    			LocalDate firstSaturday = novemberFirst.plusDays(6 - dayOfWeekNovemberFirst.getValue());
    			
    			return firstSaturday.plusDays(7);	
    		case FIRST_DAY_DECEMBER:
    			return LocalDate.of(incidenceDate.getYear(), 12, 1);
    		case EVERY_FIFTH_DAY:
    			return LocalDate.of(incidenceDate.getYear(), incidenceDate.getMonth(), 5);
    		case EVERY_NINTH_DAY:
    			return LocalDate.of(incidenceDate.getYear(), incidenceDate.getMonth(), 9);
    		case EVERY_FOURTEENTH_DAY:
    			return LocalDate.of(incidenceDate.getYear(), incidenceDate.getMonth(), 14);	
    		case EVERY_SEVENTEENTH_DAY:
    			return LocalDate.of(incidenceDate.getYear(), incidenceDate.getMonth(), 17);
    		case EVERY_TWENTY_NINTH_DAY:
    			return LocalDate.of(incidenceDate.getYear(), incidenceDate.getMonth(), 29);
    		default:
    			return null;
    		}
    	}
    	else {
	    	if(lastEstimatedDay == null) {
	    		return estimatedDate;
	    	}
	    	else {
	    	    
	    	    long diffDays = Math.abs(Duration.between(estimatedDate.atStartOfDay(), lastEstimatedDay.atStartOfDay()).toDays());
	    	    
	    	    if(periodicity.getSize() <= diffDays) {
		    		return estimatedDate;
		    	}
		    	else {
		    		return null;
		    	}
	    	    
	    	}
	    }
    }

}
