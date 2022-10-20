package com.faap.scheduler.job_application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.JobExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.UtilDateService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
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
    	this.fillSortAndSplitSheetTest();
    	//this.readPeriodicTasks();
    	//this.loadPeriodicTasks();
        assertTrue( true );
    }
    
    public void loadPeriodicTasks() {
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	//String initialSheetName = "Periodic Tasks";
    	//String finalSheetName = "Things to do";
    	
    	List<SheetCellType> initialSheetCellTypeList = new ArrayList<>();
    	
    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
    		initialSheetCellTypeList.add(new SheetCellType(periodicTask));
    	}
    	
    	List<SheetCellType> finalSheetCellTypeList = new ArrayList<>();
    	
    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
    		finalSheetCellTypeList.add(new SheetCellType(thingToDoColumnType));
    	}
    	
    	this.jobExcelService.loadThingsToDoSheet(jobExcelService, initialFilePath, finalFilePath, initialSheetCellTypeList, finalSheetCellTypeList);
    	
    }
    
    public void readPeriodicTasks() {
    	
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String sheetName = "Periodic Tasks";

    	
    	List<SheetCellType> sheetCellTypeList = new ArrayList<>();
    	
    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
    		sheetCellTypeList.add(new SheetCellType(periodicTask));
    	}
    	
    	this.readAndSaveSheet(jobExcelService, initialFilePath, finalFilePath, sheetName, sheetCellTypeList);
    	
    }
    
    private void readAndSaveSheet(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		String sheetName, List<SheetCellType> sheetCellTypeList) {
    	System.out.println("Read and Save Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.jobExcelService.readExcel(initialFilePath);

			ExcelSheet excelSheet = this.jobExcelService.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);
			
			this.jobExcelService.deleteSheet(myWorkBook, sheetName);
			this.jobExcelService.addSheetToExcel(myWorkBook, sheetName, myWorkBook.getNumberOfSheets(), sheetCellTypeList, excelSheet.getSheetRowList());
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
    	
    	int COLUMN_INDEX_TO_SORT = 4;
    	int COLUMN_INDEX_TO_FILTER = 7;
    	String TOKEN_TO_FILTER = "PENDING";
    	
    	List<SheetCellType> sheetCellTypeList = new ArrayList<>();
    	
    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
    		sheetCellTypeList.add(new SheetCellType(thingToDoColumnType));
    	}
    	
    	this.jobExcelService.fillSortSplitAndSaveSheet(initialFilePath, finalFilePath, sheetName, COMPLETE_SHEET_NAME,
    			sheetCellTypeList, COLUMN_INDEX_TO_SORT, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
    	
    }
    
   
}
