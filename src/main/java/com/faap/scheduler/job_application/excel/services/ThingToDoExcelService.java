package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.Periodicity;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.models.Weekday;
import com.faap.scheduler.job_application.file.services.SecretaryService;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDoDto;

public class ThingToDoExcelService extends AbstractApiExcelService {
	private static String THINGS_TO_DO_SHEET_NAME = "Things to do";
	private static String PERIODIC_TASKS_SHEET_NAME = "Periodic Tasks";
	public static List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";
	public static int MAXIMUN_DAYS_NUMBERA_ALLOWED = 60;

	public ThingToDoExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService, SecretaryService secretaryService) {
		super(utilDateService, utilExcelService, 
				excelReadService,excelWriteService,
				secretaryService);
	}
	
	public void loadAndSortThingsToDoSheet(ThingToDoExcelService thingToDoExcelService, String initialFilePath, String finalFilePath,
    		List<CellTypeWrapper> initialCellTypeWrapperList, List<CellTypeWrapper> finalCellTypeWrapperList) {
		System.out.println("Load Periodic Task.");
		WorkbookResponse workbookResponse = null;
		XSSFWorkbook myWorkBook = null;
		try {
			workbookResponse = this.fillSortSheet(initialFilePath, finalFilePath, 
					THINGS_TO_DO_SHEET_NAME, finalCellTypeWrapperList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
			
			if(workbookResponse.isSuccess()) {
				myWorkBook = workbookResponse.getMyWorkBook();
				
				SheetWrapper initialSheetWrapper = this.readSheet(myWorkBook, PERIODIC_TASKS_SHEET_NAME,
						initialCellTypeWrapperList);
				
				SheetWrapper finalSheetWrapper = this.readSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME,
						finalCellTypeWrapperList);
				
				
				for(RowWrapper initialRowWrapper: initialSheetWrapper.getRowWrapperList()) {
					String initialDate = null;
					String priority = null;
					String thingsToDo = null;
					String category = null;
					String periodicity = null;
					String weekday = null;
					
					for(CellWrapper initialCellWrapper: initialRowWrapper.getCellWrapperList()) {
						PeriodicTaskColumnType initialColumnType = Arrays.asList(PeriodicTaskColumnType.values()).stream().filter(ptct -> ptct.getName().equals(initialCellWrapper.getCellTypeWrapper().getName())).findFirst().orElse(null);
						switch (initialColumnType) {
						case INITIAL_DATE:
							initialDate = initialCellWrapper.getCellValue();
							break;
						case TASK:
							thingsToDo = initialCellWrapper.getCellValue();
							break;
						case PRIORITY:
							priority = initialCellWrapper.getCellValue();
							break;
						case CATEGORY: 
							category = initialCellWrapper.getCellValue();
							break;
						case PERIODICITY: 
							periodicity = initialCellWrapper.getCellValue();
							break;
						case WEEKDAY: 
							weekday = initialCellWrapper.getCellValue();
							break;	
						default:
							break;
						}
					}
					
					//1. Se calcula la fecha estimada del ultimo registro ingresado (esta puede ser nula si es la primera vez).
					//2. Se calcula la fecha asociada al día de la semana indicado en weekday (Tomando en cuenta la semana actual)
					//3. Se verifica si la diferencia en días entre la fecha del paso 1 y el paso 2 es mayor o igual al indicado en el enum periodicity. 
					//   En caso afirmativo esa es la nueva fecha estimada.
					//   En caso negativo la fecha estimada es null.
					LocalDate estimatedDate = this.getEstimatedDate(finalSheetWrapper.getRowWrapperList(), thingsToDo, periodicity, weekday, initialDate);
					
					//Se verifica que no exista una tarea igual con la misma fecha estimada para no duplicar registros
					//Si la fecha estimada es NULL se verifica que no exista el nombre de la tarea para no duplicar registros.
					if(!this.existCellWrapper(finalSheetWrapper.getRowWrapperList(), thingsToDo, estimatedDate)) {
						//Se cacula la prioridad de acuedo la diferencia de días entre la fecha actual y la fecha estimada (configurado en el enum Priority)
						priority = this.utilExcelService.getPriority(estimatedDate).toString();
						
						int lastRowNumber = finalSheetWrapper.getRowWrapperList().get(finalSheetWrapper.getRowWrapperList().size() - 1).getRowNumber();
						Row row = this.createBodyRow(myWorkBook, myWorkBook.getSheet(THINGS_TO_DO_SHEET_NAME), finalCellTypeWrapperList, lastRowNumber + 1);
						
						List<Cell> cellList = this.utilExcelService.getCellList(row, finalCellTypeWrapperList);
						
						RowWrapper rowWrapper = this.excelReadService.addRowWrapper(finalSheetWrapper, cellList, finalCellTypeWrapperList, row);
						
						for(CellWrapper cellWrapper: rowWrapper.getCellWrapperList()) {
							this.completeCellWrapper(cellWrapper, row.getRowNum(), this.utilDateService.getStrDate(estimatedDate), priority, thingsToDo, category);
						}
					}
					
				}
				
				this.deleteSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME);
				this.addSheetToExcel(myWorkBook, THINGS_TO_DO_SHEET_NAME, 0, finalCellTypeWrapperList, finalSheetWrapper.getRowWrapperList());
				this.sortSheet(myWorkBook, THINGS_TO_DO_SHEET_NAME, finalCellTypeWrapperList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
				
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
	public void completeCellWrapperList(List<CellWrapper> wrapperCellList, XSSFWorkbook myWorkBook, int rowNumber) {
		List<CellWrapper> incompleteCellWrapperList = this.utilExcelService.calculateIncompleteCellWrapper(wrapperCellList);
		
		for(CellWrapper cellWrapper: incompleteCellWrapperList) {
			this.completeCellWrapper(cellWrapper, rowNumber, null, null, null, null);
		}
		
	}
	
	private void completeCellWrapper(
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
					+ " / columnName: " + cellWrapper.getCellTypeWrapper().getName() + " / Value: " + cellWrapper.getCellValue());
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
				cellWrapper.setCellValue(cellWrapper.getCellTypeWrapper().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(priority);
			}
			break;
		case THINGS_TO_DO:
			if(thingsToDo == null) {
				cellWrapper.setCellValue(cellWrapper.getCellTypeWrapper().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(thingsToDo);
			}
			break;	
		case CATEGORY:
			if(category == null) {
				cellWrapper.setCellValue(cellWrapper.getCellTypeWrapper().getDefaultValue().toString());
			}
			else {
				cellWrapper.setCellValue(category);
			}
			break;	
		case STATUS:
			cellWrapper.setCellValue(cellWrapper.getCellTypeWrapper().getDefaultValue().toString());
			break;
		default:
			break;
		}
		System.out.println("Completing Row: " + (rowNumber + 1) 
				+ " / columnName: " + cellWrapper.getCellTypeWrapper().getName() + " / Fixed Value: " + cellWrapper.getCellValue());
	}
	
	@Override
	public void updateCellIdValue(List<RowWrapper> wrapperRowList) {
		for (RowWrapper rowWrapper : wrapperRowList) {
			CellWrapper wrapperCellId = rowWrapper.getCellWrapperList().stream()
					.filter(sc -> sc.getCellTypeWrapper().getName().equals(ThingToDoColumnType.ID.getName())).findFirst().orElse(null);
			
			if (!String.valueOf(rowWrapper.getRowNumber() + 1).equals(wrapperCellId.getCellValue())) {
				wrapperCellId.setCellValue(String.valueOf(rowWrapper.getRowNumber() + 1));
			}
		}
	}
	
	@Override
	public void updatePriority(String sheetName, List<RowWrapper> wrapperRowList) {
		if(THINGS_TO_DO_SHEET_NAME.equals(sheetName)) {
			for (RowWrapper rowWrapper : wrapperRowList) {
				CellWrapper wrapperCellPriority = rowWrapper.getCellWrapperList().stream()
						.filter(sc -> sc.getCellTypeWrapper().getName().equals(ThingToDoColumnType.PRIORITY.getName())).findFirst().orElse(null);
				
				CellWrapper wrapperCellEstimatedDate = rowWrapper.getCellWrapperList().stream()
						.filter(sc -> sc.getCellTypeWrapper().getName().equals(ThingToDoColumnType.ESTIMATED_DATE.getName())).findFirst().orElse(null);
				
				if (wrapperCellEstimatedDate.getCellValue() != null) {
					wrapperCellPriority.setCellValue(this.utilExcelService.getPriority(this.utilDateService.getLocalDate(wrapperCellEstimatedDate.getCellValue())).toString());
				}
			}
		}
	}
	
	private boolean existCellWrapper(List<RowWrapper> wrapperRowList, String thingsToDo, 
			LocalDate estimatedDate) {
		
		return this.findFirstRowWrapper(wrapperRowList, thingsToDo, this.utilDateService.getStrDate(estimatedDate)) != null;
	}
	
	private RowWrapper findFirstRowWrapper(List<RowWrapper> wrapperRowList, String thingsToDo, String estimatedDate) {
		return wrapperRowList.stream().filter(rw -> 
		{
			boolean thingsToDoMatched = false;
			boolean estimatedDateMatched = false;
			for(CellWrapper cellWrapper: rw.getCellWrapperList()) {
				ThingToDoColumnType thingToDoColumnType = this.getThingToDoColumnType(cellWrapper);
				if(thingsToDo == null) {
					thingsToDoMatched = true;
				}
				else if(thingToDoColumnType == ThingToDoColumnType.THINGS_TO_DO && thingsToDo.equals(cellWrapper.getCellValue())) {
					thingsToDoMatched = true;
				}
				
				if(estimatedDate == null) {
					estimatedDateMatched = true;
				}
				else if(thingToDoColumnType == ThingToDoColumnType.ESTIMATED_DATE && estimatedDate.equals(cellWrapper.getCellValue())) {
					estimatedDateMatched = true;
				}
			}
			if(thingsToDoMatched && estimatedDateMatched) {
				return true;
			}
			else {
				return false;
			}
		}).findFirst().orElse(null);
	}
	
	private ThingToDoColumnType getThingToDoColumnType(CellWrapper cellWrapper) {
		return Arrays.asList(ThingToDoColumnType.values()).stream().filter(tct -> tct.getName().equals(cellWrapper.getCellTypeWrapper().getName())).findFirst().orElse(null);
	}
	
	private LocalDate getEstimatedDate(List<RowWrapper> wrapperRowList, String thingsToDo, 
			String periodicity, String weekday, 
    		String initialDate) {
		LocalDate initialDateOfPeriodicTask = this.utilDateService.getLocalDate(initialDate);
		Periodicity periodicityEnum = Periodicity.getPeriodicity(periodicity);
		Weekday weekdayEnum = Weekday.getWeekday(weekday);
		
		//Se ordenan los registros por fecha estimada de forma descendente
		List<RowWrapper> sortedRowWrapperList = this.utilExcelService.sortRowWrapperList(wrapperRowList, 
				Arrays.asList(ThingToDoColumnType.ESTIMATED_DATE.getColumnIndex()), COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
		
		//Se busca el primer que coincida con thingsToDo
		RowWrapper lastRowWrapperWithEstimatedDate = this.findFirstRowWrapper(sortedRowWrapperList, thingsToDo, null);
		
		//Se calcula la ultima fecha estimada
		LocalDate lastEstimatedDate = null;
		
		if(lastRowWrapperWithEstimatedDate != null) {
			CellWrapper estimatedDateCellWrapper = lastRowWrapperWithEstimatedDate.getCellWrapperList()
					.stream()
					.filter(sc -> this.getThingToDoColumnType(sc) == ThingToDoColumnType.ESTIMATED_DATE)
					.findFirst()
					.orElse(null);
			if(estimatedDateCellWrapper.getCellValue() != null) {
				lastEstimatedDate = this.utilDateService.getLocalDate(estimatedDateCellWrapper.getCellValue());
			}
		}
		
		return this.getEstimatedDate(periodicityEnum, weekdayEnum, 
				initialDateOfPeriodicTask, lastEstimatedDate);
	}
	
	private LocalDate getEstimatedDate(Periodicity periodicity, Weekday weekday, 
    		LocalDate initialDateOfPeriodicTask, LocalDate lastEstimatedDay) {
    	
		LocalDate today = LocalDate.now();
    	
    	if(periodicity.getSize() == -1 && periodicity.getMonthDayNumber() == -1) {
    		switch (periodicity) {
    		case LAST_DAY_MONTH:
    			return today.withDayOfMonth(today.getMonth().length(today.isLeapYear()));
    		case SECOND_SATURDAY_NOVEMBER:
    			LocalDate novemberFirst = LocalDate.of(today.getYear(), 11, 1);
    			while (novemberFirst.getDayOfWeek() != DayOfWeek.SATURDAY) {
    				novemberFirst = novemberFirst.plusDays(1);
    	        }
    			LocalDate firstSaturday = novemberFirst;
    			
    			return firstSaturday.plusDays(7);	
    		case SECOND_TUESDAY_OF_MONTH:
    			
    			LocalDate monthFirst = LocalDate.of(today.getYear(), today.getMonth(), 1);
    			while (monthFirst.getDayOfWeek() != DayOfWeek.TUESDAY) {
    				monthFirst = monthFirst.plusDays(1);
    	        }
    			LocalDate firstTuesday = monthFirst;
    			
    			return firstTuesday.plusDays(7);
    			
    		case FOURTH_THURSDAY_OF_MONTH:
    			
    			monthFirst = LocalDate.of(today.getYear(), today.getMonth(), 1);
    			while (monthFirst.getDayOfWeek() != DayOfWeek.THURSDAY) {
    				monthFirst = monthFirst.plusDays(1);
    	        }
    			LocalDate firstThursday = monthFirst;
    			
    			return firstThursday.plusDays(21);	
    		case FIRST_DAY_DECEMBER:
    			return LocalDate.of(today.getYear(), 12, 1);
    			
    		case EVERY_MARCH_EIGHTH:
    			return LocalDate.of(today.getYear(), 3, 8);	
    			
    		case EVERY_JUNE_THIRTEENTH:
    			return LocalDate.of(today.getYear(), 06, 13);
    			
    		case EVERY_JUNE_TWENTY_SECOND:
    			return LocalDate.of(today.getYear(), 06, 22);
    			
    		case EVERY_OCTOBER_TENTH:
    			return LocalDate.of(today.getYear(), 10, 10);	
    			
    		case EVERY_OCTOBER_TWENTIETH:
    			return LocalDate.of(today.getYear(), 10, 20);	
    			
    		case EVERY_OCTOBER_TWENTY_FOURTH:
    			return LocalDate.of(today.getYear(), 10, 24);	
  
    		default:
    			return null;
    		}
    	}
    	else if(periodicity.getMonthDayNumber() != -1) {
    		
    		try {
    			return LocalDate.of(today.getYear(), today.getMonth(), periodicity.getMonthDayNumber());
    		}
    		catch (Exception e) {
    			return today.withDayOfMonth(
						today.getMonth().length(today.isLeapYear()));
    		}
    	}
    	else {
    		
	    	if(lastEstimatedDay == null) {
	    		LocalDate estimatedDate = (weekday.getValue() != -1 
	        			? today.plusDays(weekday.getValue() - today.getDayOfWeek().getValue()) 
	        			: today);
	    		
	    		return estimatedDate;
	    	}
	    	else if(weekday.getValue() == -1) {
	    		LocalDate newEstimatedDate = lastEstimatedDay.plusDays(periodicity.getSize());
	    		long diffDays = Math.abs(Duration.between(newEstimatedDate.atStartOfDay(), lastEstimatedDay.atStartOfDay()).toDays());
	    		if(diffDays <= MAXIMUN_DAYS_NUMBERA_ALLOWED) {
	    			return newEstimatedDate;
	    		}
	    		else {
	    			return null;
	    		}
	    	}
	    	else {
	    		LocalDate estimatedDate = today.plusDays(weekday.getValue() - today.getDayOfWeek().getValue());
	    	    
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
	
	public List<RowWrapper> saveAndSortReactThingsToDo(List<ThingToDo> reactThingToDoList, 
			String initialThingToDoFileName, String finalThingToDoFileName, String sheetName,
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {
		XSSFWorkbook myWorkBook = null;
		List<RowWrapper> sortedRowWrapperList = null;
		try {
			
			if(reactThingToDoList.size() > 0) {
			
				myWorkBook = this.readExcel(initialThingToDoFileName);
				
				List<CellTypeWrapper> cellTypeWrapperList = new ArrayList<>();
		    	
		    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
		    		cellTypeWrapperList.add(new CellTypeWrapper(thingToDoColumnType));
		    	}
				
				SheetWrapper sheetWrapper = this.readSheet(myWorkBook, sheetName, cellTypeWrapperList);
				
				RowWrapper lastRowWrapper = sheetWrapper.getRowWrapperList().get(sheetWrapper.getRowWrapperList().size() - 1);
				
				List<RowWrapper> reactRowWrapperList = this.utilExcelService.getRowWrapperList(reactThingToDoList, cellTypeWrapperList, lastRowWrapper.getRowNumber());
				
				reactRowWrapperList.forEach(reactRowWrapper -> {
					sheetWrapper.getRowWrapperList().removeIf(rw -> {
						String reactToken = reactRowWrapper.getCellWrapperList().get(ThingToDoColumnType.ID.getColumnIndex()).getCellValue();
						String token = rw.getCellWrapperList().get(ThingToDoColumnType.ID.getColumnIndex()).getCellValue();
						return token.equals(reactToken);
						
					});
				});
				
				sheetWrapper.getRowWrapperList().addAll(reactRowWrapperList);
				
				sortedRowWrapperList = this.utilExcelService.sortRowWrapperList(
						sheetWrapper.getRowWrapperList(), columnIndexToSortList, columnIndexToFilter, tokenToFilter);
				
				this.updateRowNumber(sortedRowWrapperList);
				
				this.deleteSheet(myWorkBook, sheetName);
				this.addSheetToExcel(myWorkBook, sheetName, 0, cellTypeWrapperList, sortedRowWrapperList);
				
				System.out.println("importReactThingsToDo - Saving WorkBook.");
				this.writeExcel(myWorkBook, finalThingToDoFileName);	
				
			}
			return sortedRowWrapperList;
		}
		catch(Exception e) {
			e.printStackTrace();
			return sortedRowWrapperList;
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
	
	public void saveAllThingsToDo(String initialThingToDoFileName, String sheetName) {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(initialThingToDoFileName);
			
			List<CellTypeWrapper> cellTypeWrapperList = new ArrayList<>();
	    	
	    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
	    		cellTypeWrapperList.add(new CellTypeWrapper(thingToDoColumnType));
	    	}
			
			SheetWrapper sheetWrapper = this.readSheet(myWorkBook, sheetName, cellTypeWrapperList);
			List<RowWrapper> rowWrapperList = sheetWrapper.getRowWrapperList();
			
			List<ThingToDoDto> thingToDoDtoList = new ArrayList<>();
			for(RowWrapper rowWrapper: rowWrapperList) {
				
				ThingToDoDto thingToDoDto = new ThingToDoDto(rowWrapper);
				thingToDoDtoList.add(thingToDoDto);
				
			}
			
			this.secretaryService.saveAllThingToDoList(thingToDoDtoList);
		}
		catch(Exception e) {
			e.printStackTrace();
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

}
