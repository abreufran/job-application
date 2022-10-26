package com.faap.scheduler.job_application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.JobExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.file.services.UtilFileService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private UtilFileService utilFileService;
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;
	private ExcelWriteService excelWriteService;
	private ExcelReadService excelReadService; 
	
	private JobExcelService jobExcelService;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	
        TestSuite testSuite = new TestSuite( AppTest.class );
        return testSuite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	//this.createExcelTest1();
    	//this.createExcelTest2();
    	//this.fillSortAndSplitSheetTest();
    	//this.readPeriodicTasks();
    	this.loadPeriodicTasks();
    	
    	//this.calculateEstimatedDate();
        assertTrue( true );
    }
    
    public void loadPeriodicTasks() {
    	this.utilFileService = new UtilFileService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	//String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	//String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	
    	String initialFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	
    	String backupPath = "/Users/acidlabs/Desktop/job_backup/backup";
    	
    	String destinationFile = this.utilFileService.makeBackup(backupPath, initialFilePath);

    	if(destinationFile != null) {
    	
	    	List<CellTypeWrapper> initialSheetCellTypeList = new ArrayList<>();
	    	
	    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
	    		initialSheetCellTypeList.add(new CellTypeWrapper(periodicTask));
	    	}
	    	
	    	List<CellTypeWrapper> finalSheetCellTypeList = new ArrayList<>();
	    	
	    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
	    		finalSheetCellTypeList.add(new CellTypeWrapper(thingToDoColumnType));
	    	}
	    	
	    	this.jobExcelService.loadAndSortThingsToDoSheet(jobExcelService, initialFilePath, finalFilePath, initialSheetCellTypeList, finalSheetCellTypeList);
    	}
    	
    }
    
    
    /*public void calculateEstimatedDate() {
    	LocalDate initialDateOfPeriodicTask = LocalDate.of(2020, 10, 10);
    	LocalDate lastEstimatedDay = LocalDate.of(2020, 10, 3);
    	
    	LocalDate estimatedDate = this.getEstimatedDate(Periodicity.EVERY_2_WEEKS, Weekday.MONDAY, 
    			initialDateOfPeriodicTask, LocalDate.now(), lastEstimatedDay);
    	System.out.println(estimatedDate);
    }*/
    
    /*private LocalDate getEstimatedDate(Periodicity periodicity, Weekday weekday, 
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
    		case FIRST_DAY_DECEMBER:
    			return LocalDate.of(incidenceDate.getYear(), 12, 1);
    		default:
    			return null;
    		}
    	}
    	else {
	    	if(lastEstimatedDay == null) {
	    		return estimatedDate;
	    	}
	    	else {
	    		Period period = Period.between(estimatedDate, lastEstimatedDay);
	    	    int diff = Math.abs(period.getDays());
	    	    
	    	    
	    	    if(periodicity.getSize() <= diff) {
		    		return estimatedDate;
		    	}
		    	else {
		    		return null;
		    	}
	    	    
	    	}
	    }
    }*/
    
    
    public void readPeriodicTasks() {
    	
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String sheetName = "Periodic Tasks";

    	
    	List<CellTypeWrapper> wrapperCellTypeList = new ArrayList<>();
    	
    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
    		wrapperCellTypeList.add(new CellTypeWrapper(periodicTask));
    	}
    	
    	this.readAndSaveSheet(jobExcelService, initialFilePath, finalFilePath, sheetName, wrapperCellTypeList);
    	
    }
    
    private void readAndSaveSheet(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
    	System.out.println("Read and Save Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.jobExcelService.readExcel(initialFilePath);

			SheetWrapper sheetWrapper = this.jobExcelService.readSheet(myWorkBook, sheetName,
					wrapperCellTypeList);
			
			this.jobExcelService.deleteSheet(myWorkBook, sheetName);
			this.jobExcelService.addSheetToExcel(myWorkBook, sheetName, myWorkBook.getNumberOfSheets(), wrapperCellTypeList, sheetWrapper.getSheetRowList());
			System.out.println("readAndSaveSheet - Saving WorkBook.");
			this.jobExcelService.writeExcel(myWorkBook, finalFilePath);
			
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
    
    public void fillSortAndSplitSheetTest() {
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String sheetName = "Things to do";
    	String COMPLETE_SHEET_NAME = "Complete Things";
    	
    	List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
    	int COLUMN_INDEX_TO_FILTER = 7;
    	String TOKEN_TO_FILTER = "PENDING";
    	
    	List<CellTypeWrapper> wrapperCellTypeList = new ArrayList<>();
    	
    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
    		wrapperCellTypeList.add(new CellTypeWrapper(thingToDoColumnType));
    	}
    	
    	this.jobExcelService.fillSortSplitAndSaveSheet(initialFilePath, finalFilePath, sheetName, COMPLETE_SHEET_NAME,
    			wrapperCellTypeList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
    	
    }
    
   
}
