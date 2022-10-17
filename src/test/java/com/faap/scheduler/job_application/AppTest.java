package com.faap.scheduler.job_application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
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
    	//this.fillSortAndSplitSheetTest();
    	this.readPeriodicTasks();
        assertTrue( true );
    }
    
    private void readPeriodicTasks() {
    	
    	this.utilDateService = new UtilDateService();
    	this.utilExcelService = new UtilExcelService(utilDateService);
    	this.excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	this.excelWriteService = new ExcelWriteService(utilDateService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String SHEET_NAME = "Periodic Tasks";

    	
    	List<SheetCellType> SHEET_CELL_TYPE_LIST = 
    			Arrays.asList(SheetCellType.ID, SheetCellType.INCIDENCE_DATE, 
    					SheetCellType.EXECUTION_DATE, SheetCellType.ESTIMATED_DATE,
    					SheetCellType.PRIORITY, SheetCellType.THINGS_TO_DO,
    					SheetCellType.CATEGORY, SheetCellType.STATUS);
    	
    	this.readAndSaveSheet(jobExcelService, initialFilePath, finalFilePath, SHEET_NAME, SHEET_CELL_TYPE_LIST);
    	
    }
    
    private void readAndSaveSheet(JobExcelService jobExcelService, String initialFilePath, String finalFilePath,
    		String sheetName, List<SheetCellType> sheetCellTypeList) {
    	System.out.println("Read and Save Sheet. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.jobExcelService.getExcelReadService().readExcel(initialFilePath);

			ExcelSheet excelSheet = this.jobExcelService.getExcelReadService().readSheet(myWorkBook, sheetName,
					sheetCellTypeList);
			
			this.jobExcelService.getExcelWriteService().deleteSheet(myWorkBook, sheetName);
			this.jobExcelService.getExcelWriteService().addSheetToExcel(myWorkBook, sheetName, 0, sheetCellTypeList, excelSheet.getSheetRowList());
			System.out.println("readAndSaveSheet - Saving WorkBook.");
			this.jobExcelService.getExcelWriteService().writeExcel(myWorkBook, finalFilePath);
			
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
    	this.excelWriteService = new ExcelWriteService(utilDateService);
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String SHEET_NAME = "Things to do";
    	String COMPLETE_SHEET_NAME = "Complete Things";
    	
    	int COLUMN_INDEX_TO_SORT = 4;
    	int COLUMN_INDEX_TO_FILTER = 7;
    	String TOKEN_TO_FILTER = "PENDING";
    	
    	List<SheetCellType> SHEET_CELL_TYPE_LIST = 
    			Arrays.asList(SheetCellType.ID, SheetCellType.INCIDENCE_DATE, 
    					SheetCellType.EXECUTION_DATE, SheetCellType.ESTIMATED_DATE,
    					SheetCellType.PRIORITY, SheetCellType.THINGS_TO_DO,
    					SheetCellType.CATEGORY, SheetCellType.STATUS);
    	
    	this.jobExcelService.fillSortSplitAndSaveSheet(initialFilePath, finalFilePath, SHEET_NAME, COMPLETE_SHEET_NAME,
    			SHEET_CELL_TYPE_LIST, COLUMN_INDEX_TO_SORT, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
    	
    }
    
   
}
