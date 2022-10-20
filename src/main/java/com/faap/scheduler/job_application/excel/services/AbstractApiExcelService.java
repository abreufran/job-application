package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public abstract class AbstractApiExcelService {
	
	protected UtilDateService utilDateService;
	protected UtilExcelService utilExcelService;
	protected ExcelReadService excelReadService;
	protected ExcelWriteService excelWriteService;
	
	public AbstractApiExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		this.setUtilDateService(utilDateService);
		this.setUtilExcelService(utilExcelService);
		this.setExcelReadService(excelReadService);
		this.setExcelWriteService(excelWriteService);
	}
	
	public abstract void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook, int rowNumber);
	
	public abstract void updateCellIdValue(List<SheetRow> sheetRowList);
	
	public boolean fillSortSplitAndSaveSheet(String initialFilePath, String finalFilePath, String sheetNameOfTokenFilter, 
			String sheetNameNoTokenFilter, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {
		WorkbookResponse workbookResponse = this.fillSortSplitSheet(initialFilePath, finalFilePath, 
				sheetNameOfTokenFilter, sheetNameNoTokenFilter, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "SplitSheet");
	}
	
	public WorkbookResponse fillSortSplitSheet(String initialFilePath, String finalFilePath, String sheetNameOfTokenFilter, 
			String sheetNameNoTokenFilter, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = null;
		try {
			workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetNameOfTokenFilter, sheetCellTypeList);
			
			if(!workbookResponse.isSuccess()) {
				
				return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
			}
			
			boolean fillChanged = workbookResponse.isChanged();
			
			workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);
			
			if(!workbookResponse.isSuccess()) {
				return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
			}
			
			boolean sortChanged = workbookResponse.isChanged();
			
			//Add sheet if doesn't it exist
			if(!this.utilExcelService.existSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter)) {
				this.addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, sheetCellTypeList);
			}
			
			//Add sheet if doesn't it exist
			if(!this.utilExcelService.existSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter)) {	
				this.addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(),sheetCellTypeList);
			}

			//Read sheet
			ExcelSheet excelSheetOfTokenFilet = this.readSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter,
					sheetCellTypeList);
			
			ExcelSheet excelSheetNoTokenFilet = this.readSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter,
					sheetCellTypeList);
			
			//Split sheet by token filter
			List<SheetRow> sheetRowOfTokenFilterList = excelSheetOfTokenFilet.getSheetRowList().stream().filter(sr -> {
				return sr.getSheetCellList().get(columnIndexToFilter).getCellValue().equals(tokenToFilter);
			}).collect(Collectors.toList());
			
			//Split sheet by NO token filter
			List<SheetRow> sheetRowNoTokenFilterList = excelSheetOfTokenFilet.getSheetRowList().stream().filter(sr -> {
				return !sr.getSheetCellList().get(columnIndexToFilter).getCellValue().equals(tokenToFilter);
			}).collect(Collectors.toList());
			
			//Add old records of NO token filer
			sheetRowNoTokenFilterList.addAll(excelSheetNoTokenFilet.getSheetRowList());
			
			//Sort records of NO token filter
			Comparator<SheetRow> priorityComparator = (sr1, sr2) -> {
				String cellValueToSort1 = sr1.getSheetCellList().get(columnIndexToSort).getCellValue();
				String cellValueToSort2 = sr2.getSheetCellList().get(columnIndexToSort).getCellValue();
				return cellValueToSort1.compareTo(cellValueToSort2);
			};
			sheetRowNoTokenFilterList = sheetRowNoTokenFilterList.stream().sorted(priorityComparator).collect(Collectors.toList());


			if (excelSheetOfTokenFilet.getSheetRowList().size() != sheetRowOfTokenFilterList.size()
					|| excelSheetNoTokenFilet.getSheetRowList().size() != sheetRowNoTokenFilterList.size()
					|| fillChanged || sortChanged) {
				this.updateRowNumber(sheetRowOfTokenFilterList);
				this.updateRowNumber(sheetRowNoTokenFilterList);
				
				this.deleteSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter);
				this.addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, sheetCellTypeList, sheetRowOfTokenFilterList);
				
				this.deleteSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter);
				this.addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(), sheetCellTypeList, sheetRowNoTokenFilterList);


				return new WorkbookResponse(workbookResponse.getMyWorkBook(), true, true);
			}
			else {
				return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, true);
			}
				

			
		} catch (Exception e) {
			e.printStackTrace();
			this.closeWorkBook(workbookResponse.getMyWorkBook());
			return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
		}
	}
	
	public boolean fillSortAndSaveSheet(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = this.fillSortSheet(initialFilePath, finalFilePath, 
				sheetName, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "SortSheet");
	}
	
	public WorkbookResponse fillSortSheet(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, sheetCellTypeList);
			
		if(!workbookResponse.isSuccess()) {
			
			return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
		}
		
		boolean fillChanged = workbookResponse.isChanged();
		
		workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetName, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);

		workbookResponse.setChanged(fillChanged || workbookResponse.isChanged());
		
		return workbookResponse;
	}

	
	private WorkbookResponse sortSheet(XSSFWorkbook myWorkBook, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {
		System.out.println("Sort Sheet. ");
		try {

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);
			
			List<SheetRow> sortedSheetRowList = this.utilExcelService.sortSheetRowList(
					excelSheet.getSheetRowList(), columnIndexToSort, columnIndexToFilter, tokenToFilter);


			if (this.utilExcelService.didSheetSort(sortedSheetRowList)) {
				this.updateRowNumber(sortedSheetRowList);
				
				this.deleteSheet(myWorkBook, sheetName);
				this.addSheetToExcel(myWorkBook, sheetName, 0, sheetCellTypeList, sortedSheetRowList);
				return new WorkbookResponse(myWorkBook, true, true);
			}
			else {
				return new WorkbookResponse(myWorkBook, false, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.closeWorkBook(myWorkBook);
			return new WorkbookResponse(null, false, false);
		} 
	}
	
	public boolean fillAndSaveEmptyFields(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		return this.fillAndSaveEmptyFields(filePath, filePath, sheetName, sheetCellTypeList);
	}
	
	public boolean fillAndSaveEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		WorkbookResponse workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, sheetCellTypeList);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "FillSheet");
	}
	
	private WorkbookResponse fillEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		System.out.println("Fill empty fields. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);

			ExcelSheet excelSheet = this.readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			List<SheetRow> incompleteSheetRowList = this.utilExcelService.calculateIncompleteSheetRowList(excelSheet.getSheetRowList());

			for(SheetRow sheetRow: incompleteSheetRowList) {
				this.completeSheetCellList(sheetRow.getSheetCellList(), myWorkBook, sheetRow.getRowNumber());
			}

			if (incompleteSheetRowList.size() > 0) {
				this.deleteSheet(myWorkBook, sheetName);
				this.addSheetToExcel(myWorkBook, sheetName, 0, sheetCellTypeList, excelSheet.getSheetRowList());
				return new WorkbookResponse(myWorkBook, true, true);
			}
			else {
				return new WorkbookResponse(myWorkBook, false, true);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			this.closeWorkBook(myWorkBook);
			return new WorkbookResponse(null, false, false);
		}
	}
	
	public boolean saveWorkbookResponse(WorkbookResponse workbookResponse, String finalFilePath, String operationName) {
		try {
			if(!workbookResponse.isSuccess()) {
				return false;
			}

			if (workbookResponse.isChanged()) {
				System.out.println(operationName + " - Saving WorkBook.");
				this.writeExcel(workbookResponse.getMyWorkBook(), finalFilePath);
				return true;
			}
			else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			this.closeWorkBook(workbookResponse.getMyWorkBook());
		}
	}
	
	private void closeWorkBook(XSSFWorkbook myWorkBook) {
		if (myWorkBook != null) {
			try {
				myWorkBook.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void addEmptySheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<SheetCellType> sheetCellTypeList) {
		this.excelWriteService.addEmptySheetToExcel(myWorkBook, sheetName, columnIndex, sheetCellTypeList);
	}
	
	public void addSheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<SheetCellType> sheetCellTypeList, List<SheetRow> sheetRowList) {
		this.excelWriteService.addSheetToExcel(myWorkBook, sheetName, columnIndex, sheetCellTypeList, sheetRowList);
	}

	
	public void deleteSheet(XSSFWorkbook myWorkBook, String sheetName) {
		this.excelWriteService.deleteSheet(myWorkBook, sheetName);
	}
	
	public void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		this.excelWriteService.writeExcel(myWorkBook, filePath);
	}
	
	public ExcelSheet readSheet(XSSFWorkbook myWorkBook, String sheetName, List<SheetCellType> sheeCellTypeList) throws Exception {
		ExcelSheet excelSheet = this.excelReadService.readSheet(myWorkBook, sheetName, sheeCellTypeList);
		this.updateCellIdValue(excelSheet.getSheetRowList());
		return excelSheet;
		
	}
	
	public Row createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<SheetCellType> sheeCellTypeList, int rowNumber) {
		return this.excelWriteService.createBodyRow(myWorkBook, sheet, sheeCellTypeList, rowNumber);
	}
	
	public XSSFWorkbook readExcel(String filePath) throws IOException {
		return this.excelReadService.readExcel(filePath);
	}
	
	
	private void updateRowNumber(List<SheetRow> sortedSheetRowList) {
		int rowNumber = 0; //Header
		for (SheetRow sheetRow: sortedSheetRowList) {
			rowNumber++;
			sheetRow.setRowNumber(rowNumber);
		}
		this.updateCellIdValue(sortedSheetRowList);
	}


	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}

	public void setExcelReadService(ExcelReadService excelReadService) {
		this.excelReadService = excelReadService;
	}

	public void setExcelWriteService(ExcelWriteService excelWriteService) {
		this.excelWriteService = excelWriteService;
	}

	public void setUtilExcelService(UtilExcelService utilExcelService) {
		this.utilExcelService = utilExcelService;
	}
}