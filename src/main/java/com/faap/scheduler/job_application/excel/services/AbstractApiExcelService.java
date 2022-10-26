package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
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
	
	public abstract void completeSheetCellList(List<CellWrapper> wrapperCellList, XSSFWorkbook myWorkBook, int rowNumber);
	
	public abstract void updateCellIdValue(List<RowWrapper> wrapperRowList);
	
	public abstract void updatePriority(String sheetName, List<RowWrapper> wrapperRowList);
	
	public boolean fillSortSplitAndSaveSheet(String initialFilePath, String finalFilePath, String sheetNameOfTokenFilter, 
			String sheetNameNoTokenFilter, List<CellTypeWrapper> wrapperCellTypeList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {
		WorkbookResponse workbookResponse = this.fillSortSplitSheet(initialFilePath, finalFilePath, 
				sheetNameOfTokenFilter, sheetNameNoTokenFilter, wrapperCellTypeList, columnIndexToSortList, columnIndexToFilter, tokenToFilter);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "SplitSheet");
	}
	
	public WorkbookResponse fillSortSplitSheet(String initialFilePath, String finalFilePath, String sheetNameOfTokenFilter, 
			String sheetNameNoTokenFilter, List<CellTypeWrapper> wrapperCellTypeList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = null;
		try {
			workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetNameOfTokenFilter, wrapperCellTypeList);
			
			if(!workbookResponse.isSuccess()) {
				
				return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
			}
			
			boolean fillChanged = workbookResponse.isChanged();
			
			workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, wrapperCellTypeList, columnIndexToSortList, columnIndexToFilter, tokenToFilter);
			
			if(!workbookResponse.isSuccess()) {
				return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
			}
			
			boolean sortChanged = workbookResponse.isChanged();
			
			//Add sheet if doesn't it exist
			if(!this.utilExcelService.existSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter)) {
				this.addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, wrapperCellTypeList);
			}
			
			//Add sheet if doesn't it exist
			if(!this.utilExcelService.existSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter)) {	
				this.addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(),wrapperCellTypeList);
			}

			//Read sheet
			SheetWrapper wrapperSheetOfTokenFilet = this.readSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter,
					wrapperCellTypeList);
			
			SheetWrapper wrapperSheetNoTokenFilet = this.readSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter,
					wrapperCellTypeList);
			
			//Split sheet by token filter
			List<RowWrapper> wrapperRowOfTokenFilterList = wrapperSheetOfTokenFilet.getSheetRowList().stream().filter(sr -> {
				return sr.getSheetCellList().get(columnIndexToFilter).getCellValue().equals(tokenToFilter);
			}).collect(Collectors.toList());
			
			//Split sheet by NO token filter
			List<RowWrapper> wrapperRowNoTokenFilterList = wrapperSheetOfTokenFilet.getSheetRowList().stream().filter(sr -> {
				return !sr.getSheetCellList().get(columnIndexToFilter).getCellValue().equals(tokenToFilter);
			}).collect(Collectors.toList());
			
			//Add old records of NO token filer
			wrapperRowNoTokenFilterList.addAll(wrapperSheetNoTokenFilet.getSheetRowList());
			
			//Sort records of NO token filter
			wrapperRowNoTokenFilterList = this.utilExcelService.sortSheetRowList(wrapperRowNoTokenFilterList, columnIndexToSortList, null);

			if (wrapperSheetOfTokenFilet.getSheetRowList().size() != wrapperRowOfTokenFilterList.size()
					|| wrapperSheetNoTokenFilet.getSheetRowList().size() != wrapperRowNoTokenFilterList.size()
					|| fillChanged || sortChanged) {
				this.updateRowNumber(wrapperRowOfTokenFilterList);
				this.updateRowNumber(wrapperRowNoTokenFilterList);
				
				this.deleteSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter);
				this.addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, wrapperCellTypeList, wrapperRowOfTokenFilterList);
				
				this.deleteSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter);
				this.addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(), wrapperCellTypeList, wrapperRowNoTokenFilterList);


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
	
	public boolean fillSortAndSaveSheet(String initialFilePath, String finalFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = this.fillSortSheet(initialFilePath, finalFilePath, 
				sheetName, wrapperCellTypeList, columnIndexToSortList, columnIndexToFilter, tokenToFilter);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "SortSheet");
	}
	
	public WorkbookResponse fillSortSheet(String initialFilePath, String finalFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, wrapperCellTypeList);
			
		if(!workbookResponse.isSuccess()) {
			
			return new WorkbookResponse(workbookResponse.getMyWorkBook(), false, false);
		}
		
		boolean fillChanged = workbookResponse.isChanged();
		
		workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetName, wrapperCellTypeList, columnIndexToSortList, columnIndexToFilter, tokenToFilter);

		workbookResponse.setChanged(fillChanged || workbookResponse.isChanged());
		
		return workbookResponse;
	}

	
	protected WorkbookResponse sortSheet(XSSFWorkbook myWorkBook, String sheetName, List<CellTypeWrapper> wrapperCellTypeList, 
			List<Integer> columnIndexToSortList, int columnIndexToFilter, String tokenToFilter) {
		System.out.println("Sort Sheet. ");
		try {

			SheetWrapper sheetWrapper = this.readSheet(myWorkBook, sheetName,
					wrapperCellTypeList);
			
			List<RowWrapper> sortedSheetRowList = this.utilExcelService.sortSheetRowList(
					sheetWrapper.getSheetRowList(), columnIndexToSortList, columnIndexToFilter, tokenToFilter);


			if (this.utilExcelService.didSheetSort(sortedSheetRowList)) {
				this.updateRowNumber(sortedSheetRowList);
				
				this.deleteSheet(myWorkBook, sheetName);
				this.addSheetToExcel(myWorkBook, sheetName, 0, wrapperCellTypeList, sortedSheetRowList);
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
	
	public boolean fillAndSaveEmptyFields(String filePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		return this.fillAndSaveEmptyFields(filePath, filePath, sheetName, wrapperCellTypeList);
	}
	
	public boolean fillAndSaveEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		WorkbookResponse workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, wrapperCellTypeList);
		
		return this.saveWorkbookResponse(workbookResponse, finalFilePath, "FillSheet");
	}
	
	private WorkbookResponse fillEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<CellTypeWrapper> wrapperCellTypeList) {
		System.out.println("Fill empty fields. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.excelReadService.readExcel(initialFilePath);

			SheetWrapper sheetWrapper = this.readSheet(myWorkBook, sheetName,
					wrapperCellTypeList);

			List<RowWrapper> incompleteSheetRowList = this.utilExcelService.calculateIncompleteSheetRowList(sheetWrapper.getSheetRowList());

			for(RowWrapper rowWrapper: incompleteSheetRowList) {
				this.completeSheetCellList(rowWrapper.getSheetCellList(), myWorkBook, rowWrapper.getRowNumber());
			}

			if (incompleteSheetRowList.size() > 0) {
				this.deleteSheet(myWorkBook, sheetName);
				this.addSheetToExcel(myWorkBook, sheetName, 0, wrapperCellTypeList, sheetWrapper.getSheetRowList());
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
	
	public void addEmptySheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<CellTypeWrapper> wrapperCellTypeList) {
		this.excelWriteService.addEmptySheetToExcel(myWorkBook, sheetName, columnIndex, wrapperCellTypeList);
	}
	
	public void addSheetToExcel(XSSFWorkbook myWorkBook, String sheetName, int columnIndex, List<CellTypeWrapper> wrapperCellTypeList, List<RowWrapper> wrapperRowList) {
		this.excelWriteService.addSheetToExcel(myWorkBook, sheetName, columnIndex, wrapperCellTypeList, wrapperRowList);
	}

	
	public void deleteSheet(XSSFWorkbook myWorkBook, String sheetName) {
		this.excelWriteService.deleteSheet(myWorkBook, sheetName);
	}
	
	public void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		this.excelWriteService.writeExcel(myWorkBook, filePath);
	}
	
	public SheetWrapper readSheet(XSSFWorkbook myWorkBook, String sheetName, List<CellTypeWrapper> sheeCellTypeList) throws Exception {
		SheetWrapper sheetWrapper = this.excelReadService.readSheet(myWorkBook, sheetName, sheeCellTypeList);
		this.updateCellIdValue(sheetWrapper.getSheetRowList());
		this.updatePriority(sheetName, sheetWrapper.getSheetRowList());
		return sheetWrapper;
		
	}
	
	public Row createBodyRow(XSSFWorkbook myWorkBook, XSSFSheet sheet, List<CellTypeWrapper> sheeCellTypeList, int rowNumber) {
		return this.excelWriteService.createBodyRow(myWorkBook, sheet, sheeCellTypeList, rowNumber);
	}
	
	public XSSFWorkbook readExcel(String filePath) throws IOException {
		return this.excelReadService.readExcel(filePath);
	}
	
	
	private void updateRowNumber(List<RowWrapper> sortedSheetRowList) {
		int rowNumber = 0; //Header
		for (RowWrapper rowWrapper: sortedSheetRowList) {
			rowNumber++;
			rowWrapper.setRowNumber(rowNumber);
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