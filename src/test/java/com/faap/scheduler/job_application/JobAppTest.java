package com.faap.scheduler.job_application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.ThingToDoExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.SecretaryService;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;
import com.faap.scheduler.job_application.tasks.ThingToDoTask;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class JobAppTest 
    extends TestCase
{

	private FileBackupRepository fileBackupRepository;
	private DataFileRepository dataFileRepository;
	private UtilFileService utilFileService;
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;
	private ExcelWriteService excelWriteService;
	private ExcelReadService excelReadService; 
	private SecretaryService secretaryService;
	
	private ThingToDoExcelService thingToDoExcelService;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JobAppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	
        TestSuite testSuite = new TestSuite( JobAppTest.class );
        return testSuite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {    	
    	//this.importReactThingsToDoTest();
    	this.loadPeriodicTasks();
    	//this.readAndExportSheetTest();

    	
		//thingToDoTask.getThingToDoList();
		
        assertTrue( true );
    }
    
    
    public void importReactThingsToDoTest() {
    	System.out.println("importReactThingsToDoTest");
    	
    	this.secretaryService = new SecretaryService();
    	this.fileBackupRepository = new FileBackupRepository();
    	this.utilFileService = new UtilFileService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	this.dataFileRepository = new DataFileRepository();
    	this.secretaryService = new SecretaryService();
    	
    	//String initialFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do.xlsx";
    	//String finalFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do_prueba.xlsx";
    	//String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/test_backup";
    	
    	String initialFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/backup";
    	
    	
    	
    	ThingToDoTask thingToDoTask = new ThingToDoTask(dataFileRepository, thingToDoExcelService, utilExcelService, utilFileService, fileBackupRepository, secretaryService,
    			backupPath, initialFilePath, finalFilePath);
    	
    	try {
    		//thingToDoTask.getThingToDoList();
			//thingToDoTask.importReactThingsToDo();
    		thingToDoTask.importReactThingsToDo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void readAndExportSheetTest() {
    	System.out.println("readAndExportSheetTest");
    	this.secretaryService = new SecretaryService();
    	this.fileBackupRepository = new FileBackupRepository();
    	this.utilFileService = new UtilFileService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	this.dataFileRepository = new DataFileRepository();
    	this.secretaryService = new SecretaryService();
    	
    	//String initialFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do.xlsx";
    	//String finalFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do_prueba.xlsx";
    	//String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/test_backup";
    	
    	String initialFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/backup";
    	
    	ThingToDoTask thingToDoTask = new ThingToDoTask(dataFileRepository, thingToDoExcelService, utilExcelService, utilFileService, fileBackupRepository, secretaryService, 
    			backupPath, initialFilePath, finalFilePath);
    	
    	try {
			thingToDoTask.readAndExportThingsToDoSheet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void loadPeriodicTasks() {
    	System.out.println("loadPeriodicTasks");
    	this.secretaryService = new SecretaryService();
    	this.utilFileService = new UtilFileService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	
    	//String initialFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do.xlsx";
    	//String finalFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do_prueba.xlsx";
    	//String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/test_backup";
    	
    	String initialFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	String backupPath = "/Users/acidlabs/Desktop/thing_to_do_backup/backup";
    	
    	
    	
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
	    	
	    	this.thingToDoExcelService.loadAndSortThingsToDoSheet(thingToDoExcelService, initialFilePath, finalFilePath, initialSheetCellTypeList, finalSheetCellTypeList);
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
    	this.secretaryService = new SecretaryService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do_prueba.xlsx";
    	String sheetName = "Periodic Tasks";

    	
    	List<CellTypeWrapper> wrapperCellTypeList = new ArrayList<>();
    	
    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
    		wrapperCellTypeList.add(new CellTypeWrapper(periodicTask));
    	}
    	
    	this.readAndSaveSheet(thingToDoExcelService, initialFilePath, finalFilePath, sheetName, wrapperCellTypeList);
    	
    }
    
    private void readAndSaveSheet(ThingToDoExcelService thingToDoExcelService, String initialFilePath, String finalFilePath,
    		String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
    	System.out.println("Read and Save Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.thingToDoExcelService.readExcel(initialFilePath);

			SheetWrapper sheetWrapper = this.thingToDoExcelService.readSheet(myWorkBook, sheetName,
					wrapperCellTypeList);
			
			this.thingToDoExcelService.deleteSheet(myWorkBook, sheetName);
			this.thingToDoExcelService.addSheetToExcel(myWorkBook, sheetName, myWorkBook.getNumberOfSheets(), wrapperCellTypeList, sheetWrapper.getRowWrapperList());
			System.out.println("readAndSaveSheet - Saving WorkBook.");
			this.thingToDoExcelService.writeExcel(myWorkBook, finalFilePath);
			
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
    	this.secretaryService = new SecretaryService();
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/thing_to_do_backup/Things_to_do_prueba.xlsx";
    	String sheetName = "Things to do";
    	String COMPLETED_SHEET_NAME = "Completed Things";
    	
    	List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
    	int COLUMN_INDEX_TO_FILTER = 7;
    	String TOKEN_TO_FILTER = "PENDING";
    	
    	List<CellTypeWrapper> wrapperCellTypeList = new ArrayList<>();
    	
    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
    		wrapperCellTypeList.add(new CellTypeWrapper(thingToDoColumnType));
    	}
    	
    	this.thingToDoExcelService.fillSortSplitAndSaveSheet(initialFilePath, finalFilePath, sheetName, COMPLETED_SHEET_NAME,
    			wrapperCellTypeList, COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
    	
    }
    
   
}
