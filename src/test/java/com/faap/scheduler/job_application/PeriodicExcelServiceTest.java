package com.faap.scheduler.job_application;

import java.util.ArrayList;
import java.util.List;

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.services.DoneExcelService;
import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.GoalService;
import com.faap.scheduler.job_application.file.services.UtilDateService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PeriodicExcelServiceTest 
    extends TestCase
{


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PeriodicExcelServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	
        TestSuite testSuite = new TestSuite( PeriodicExcelServiceTest.class );
        return testSuite;
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {    	
    	this.exportDoneTasks();

		
        assertTrue( true );
    }
    
    
    
    
    public void exportDoneTasks() {
    	System.out.println("exportPeriodicTasks");
    	UtilDateService utilDateService = new UtilDateService();
    	GoalService goalService = new GoalService();
    	UtilExcelService utilExcelService = new UtilExcelService(utilDateService);
    	ExcelReadService excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	DoneExcelService doneExcelService = new DoneExcelService(excelReadService, goalService);
    	
    	String sheetName = "Periodic Tasks";
    	String initialFilePath = "/Users/acidlabs/Library/CloudStorage/GoogleDrive-easycryptolearning21@gmail.com/Mi unidad/Things_to_do.xlsx";
    	
    	List<CellTypeWrapper> sheetCellTypeList = new ArrayList<>();
    	
    	for(PeriodicTaskColumnType doneColumnType: PeriodicTaskColumnType.values()) {
    		sheetCellTypeList.add(new CellTypeWrapper(doneColumnType));
    	}
    	
    	doneExcelService.exportPeriodicTask(initialFilePath, sheetName, sheetCellTypeList);   	
    	
    }
   
}
