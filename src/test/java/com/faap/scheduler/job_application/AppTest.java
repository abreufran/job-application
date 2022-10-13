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
    	this.fillSortAndSplitSheetTest();
        assertTrue( true );
    }
    
    private void fillSortAndSplitSheetTest() {
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
    
    public void createExcelTest2() {
    	
    	this.utilDateService = new UtilDateService();
    	this.jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	String initialFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do.xlsx";
    	String finalFilePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	String SHEET_NAME = "Things to do";
    	
    	List<SheetCellType> SHEET_CELL_TYPE_LIST = 
    			Arrays.asList(SheetCellType.ID, SheetCellType.INCIDENCE_DATE, 
    					SheetCellType.EXECUTION_DATE, SheetCellType.ESTIMATED_DATE,
    					SheetCellType.PRIORITY, SheetCellType.THINGS_TO_DO,
    					SheetCellType.CATEGORY, SheetCellType.STATUS);
    	
    	this.duplicateExcelSheet(initialFilePath, finalFilePath, SHEET_NAME, SHEET_CELL_TYPE_LIST);
    	
    	
    }
    
    private boolean duplicateExcelSheet(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList) {	
    	XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);
			
			ExcelSheet excelSheet = this.excelReadService.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);
			
			this.createExcel(sheetName, sheetCellTypeList, excelSheet.getSheetRowList(), finalFilePath);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    public void createExcelTest1() {
    	this.utilDateService = new UtilDateService();
    	
    	String sheetName = "Sheet Prueba";
    	String filePath = "/Users/acidlabs/Desktop/job_backup/Things_to_do_prueba.xlsx";
    	
    	
    	List<SheetCellType> sheeCellTypeList = new ArrayList<>();
    	sheeCellTypeList.add(SheetCellType.ID);
    	sheeCellTypeList.add(SheetCellType.THINGS_TO_DO);
    	sheeCellTypeList.add(SheetCellType.STATUS);
    	
    	List<SheetCell> sheetCellList = new ArrayList<>();
    	sheetCellList.add(new SheetCell(SheetCellType.ID, "2", null));
    	sheetCellList.add(new SheetCell(SheetCellType.THINGS_TO_DO, "Buy chicken", null));
    	sheetCellList.add(new SheetCell(SheetCellType.STATUS, null, null));

    	List<SheetRow> sheetRowList = new ArrayList<>();
    	sheetRowList.add(new SheetRow(sheetCellList, 1));
    	
    	this.createExcel(sheetName, sheeCellTypeList, sheetRowList, filePath);
    }
    

    private boolean createExcel(String sheetName, List<SheetCellType> sheetCellTypeList, List<SheetRow> sheetRowList, String filePaht)  {
    	XSSFWorkbook myWorkBook = null;
    	try {
    	
	    	myWorkBook = new XSSFWorkbook();
	    	this.addSheetToExcel(myWorkBook, sheetName, sheetCellTypeList, sheetRowList);
			
	    	this.writeExcel(myWorkBook, filePaht);
	    	
	    	return true;
    	} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    private void addSheetToExcel(XSSFWorkbook myWorkBook, String sheetName, List<SheetCellType> sheetCellTypeList, List<SheetRow> sheetRowList) {

    	XSSFSheet sheet = myWorkBook.createSheet(sheetName);
    	System.out.println("createSheet: " + sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
    	
    	System.out.println("Created");
    	
    	myWorkBook.setSheetOrder(sheetName, 0);
		myWorkBook.setSelectedTab(0);
		myWorkBook.setActiveSheet(0);

		this.createHeaderRow(myWorkBook, sheet, sheetCellTypeList);

		for (SheetRow sheetRow: sheetRowList) {
			this.createBodyRow(myWorkBook, sheet, sheetRow);
		}
		
		for (SheetCellType sheetCellType: sheetCellTypeList) {	
			sheet.setColumnWidth(sheetCellType.getColumnIndex(), sheetCellType.getColumnWidth() * 256);
		}

    }
    
    private void createHeaderRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<SheetCellType> sheeCellTypeList) {
    	Row header = sheet.createRow(0);
    	System.out.println("createRow - Header: " + header.getRowNum());

		CellStyle headerStyle = myWorkBook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFFont font = ((XSSFWorkbook) myWorkBook).createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		for (SheetCellType sheetCellType: sheeCellTypeList) {
			String headerCellValue = sheetCellType.getName();

			Cell headerCell = header.createCell(sheetCellType.getColumnIndex());
			System.out.println("createCell - Header: " + headerCell.getColumnIndex());
			
			headerCell.setCellValue(headerCellValue);
			headerCell.setCellStyle(headerStyle);
		}
    }
    
    private void createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, SheetRow sheetRow) {
    	Row row = sheet.createRow(sheetRow.getRowNumber());
		System.out.println("createRow - Body: " + row.getRowNum());

		for(SheetCell sheetCell: sheetRow.getSheetCellList()) {
			Cell cell = row.createCell(sheetCell.getSheetCellType().getColumnIndex(), sheetCell.getSheetCellType().getCellType());
			System.out.println("createCell - Body: " + cell.getColumnIndex());
			
			HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
			if(sheetCell.getSheetCellType().isRequired()) {
				horizontalAlignment = HorizontalAlignment.LEFT;
			}
			
			this.setBlankCellAndCellStyle(myWorkBook, cell, sheetCell.getSheetCellType().isDate(), 
					sheetCell.getSheetCellType().getCellType() == CellType.STRING, horizontalAlignment);
			
			if (sheetCell.getSheetCellType().isDate() && sheetCell.getCellValue() != null) {
				cell.setCellValue(this.utilDateService.getLocalDate(sheetCell.getCellValue()));
				
			} else if (sheetCell.getSheetCellType().getCellType() == CellType.FORMULA) {
				cell.setCellFormula(this.calculateFormula(sheetCell, sheetRow.getRowNumber()));
				XSSFFormulaEvaluator formulaEvaluator = myWorkBook.getCreationHelper().createFormulaEvaluator();
				formulaEvaluator.evaluateFormulaCell(cell);

			} else {
				cell.setCellValue(sheetCell.getCellValue());
			}
		}
    }
    
    private String calculateFormula(SheetCell sheetCell, int rowNumber) {
    	String formula = sheetCell.getSheetCellType().getSheetFormula().getFormula();
    	if(sheetCell.getSheetCellType().getSheetFormula().getSheetFormulaValue() != null) {
	    	switch (sheetCell.getSheetCellType().getSheetFormula().getSheetFormulaValue()) {
			case ROW_NUMBER:
				formula = formula.replaceAll(sheetCell.getSheetCellType().getSheetFormula().getKey(), String.valueOf(rowNumber + 1));
				break;
			default:
				break;
			}
    	}
		return formula;
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
    
    protected void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		File myFile = new File(filePath);

		FileOutputStream outputStream = new FileOutputStream(myFile);

		myWorkBook.write(outputStream);

		outputStream.close();
		myFile = null;
		outputStream = null;
	}
}
