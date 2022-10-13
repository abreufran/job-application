package com.faap.scheduler.job_application.excel.services;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.WorkbookResponse;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.file.services.UtilDateService;

public abstract class AbstractApiExcelService {
	
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;
	private ExcelReadService excelReadService;
	private ExcelWriteService excelWriteService;
	
	public AbstractApiExcelService(UtilDateService utilDateService, UtilExcelService utilExcelService, 
			ExcelReadService excelReadService, ExcelWriteService excelWriteService) {
		this.setUtilDateService(utilDateService);
		this.setUtilExcelService(utilExcelService);
		this.setExcelReadService(excelReadService);
		this.setExcelWriteService(excelWriteService);
	}
	
	public abstract void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook, int rowNumber);
	
	public boolean fillSortSplitAndSaveSheet(String initialFilePath, String finalFilePath, String sheetNameOfTokenFilter, 
			String sheetNameNoTokenFilter, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = null;
		try {
			workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetNameOfTokenFilter, sheetCellTypeList);
			
			if(!workbookResponse.isSuccess()) {
				
				return false;
			}
			
			boolean fillChanged = workbookResponse.isChanged();
			
			workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);
			
			if(!workbookResponse.isSuccess()) {
				return false;
			}
			
			boolean sortChanged = workbookResponse.isChanged();
			
			//Add sheet if doesn't it exist
			if(!this.getUtilExcelService().existSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter)) {
				this.getExcelWriteService().addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, sheetCellTypeList);
			}
			
			//Add sheet if doesn't it exist
			if(!this.getUtilExcelService().existSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter)) {	
				this.getExcelWriteService().addEmptySheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(),sheetCellTypeList);
			}

			//Read sheet
			ExcelSheet excelSheetOfTokenFilet = this.getExcelReadService().readSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter,
					sheetCellTypeList);
			
			ExcelSheet excelSheetNoTokenFilet = this.getExcelReadService().readSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter,
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
				
				this.getExcelWriteService().deleteSheet(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter);
				this.getExcelWriteService().addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameOfTokenFilter, 0, sheetCellTypeList, sheetRowOfTokenFilterList);
				
				this.getExcelWriteService().deleteSheet(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter);
				this.getExcelWriteService().addSheetToExcel(workbookResponse.getMyWorkBook(), sheetNameNoTokenFilter, 
						workbookResponse.getMyWorkBook().getNumberOfSheets(), sheetCellTypeList, sheetRowNoTokenFilterList);


				System.out.println("splitSheet - Saving WorkBook.");
				this.getExcelWriteService().writeExcel(workbookResponse.getMyWorkBook(), finalFilePath);
				return true;
			}
			else {
				return false;
			}
				

			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (workbookResponse.getMyWorkBook() != null) {
				try {
					workbookResponse.getMyWorkBook().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean fillSortAndSaveSheet(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {

		WorkbookResponse workbookResponse = null;
		try {
			workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, sheetCellTypeList);
			
			if(!workbookResponse.isSuccess()) {
				
				return false;
			}
			
			boolean fillChanged = workbookResponse.isChanged();
			
			workbookResponse = this.sortSheet(workbookResponse.getMyWorkBook(), sheetName, sheetCellTypeList, columnIndexToSort, columnIndexToFilter, tokenToFilter);

			if (fillChanged || workbookResponse.isChanged()) {
				System.out.println("sortSheet - Saving WorkBook.");
				this.getExcelWriteService().writeExcel(workbookResponse.getMyWorkBook(), finalFilePath);
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (workbookResponse.getMyWorkBook() != null) {
				try {
					workbookResponse.getMyWorkBook().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public boolean fillAndSaveEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		WorkbookResponse workbookResponse = null;
		try {
			workbookResponse = this.fillEmptyFields(initialFilePath, finalFilePath, sheetName, sheetCellTypeList);
			
			if(!workbookResponse.isSuccess()) {
				return false;
			}

			if (workbookResponse.isChanged()) {
				System.out.println("fillEmptyFields - Saving WorkBook.");
				this.getExcelWriteService().writeExcel(workbookResponse.getMyWorkBook(), finalFilePath);
				return true;
			}
			else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (workbookResponse.getMyWorkBook() != null) {
				try {
					workbookResponse.getMyWorkBook().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private WorkbookResponse fillEmptyFields(String initialFilePath, String finalFilePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		System.out.println("Fill empty fields. ");
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.getExcelReadService().readExcel(initialFilePath);

			ExcelSheet excelSheet = this.getExcelReadService().readSheet(myWorkBook, sheetName,
					sheetCellTypeList);

			List<SheetRow> incompleteSheetRowList = this.getUtilExcelService().calculateIncompleteSheetRowList(excelSheet.getSheetRowList());

			for(SheetRow sheetRow: incompleteSheetRowList) {
				this.completeSheetCellList(sheetRow.getSheetCellList(), myWorkBook, sheetRow.getRowNumber());
			}

			if (incompleteSheetRowList.size() > 0) {
				this.getExcelWriteService().deleteSheet(myWorkBook, sheetName);
				this.getExcelWriteService().addSheetToExcel(myWorkBook, sheetName, 0, sheetCellTypeList, excelSheet.getSheetRowList());
				return new WorkbookResponse(myWorkBook, true, true);
			}
			else {
				return new WorkbookResponse(myWorkBook, false, true);
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
			return new WorkbookResponse(null, false, false);
		}
	}

	public boolean fillAndSaveEmptyFields(String filePath, String sheetName, List<SheetCellType> sheetCellTypeList) {
		return this.fillAndSaveEmptyFields(filePath, filePath, sheetName, sheetCellTypeList);
	}
	
	private WorkbookResponse sortSheet(XSSFWorkbook myWorkBook, String sheetName, List<SheetCellType> sheetCellTypeList, 
			int columnIndexToSort, int columnIndexToFilter, String tokenToFilter) {
		System.out.println("Sort Sheet. ");
		try {

			ExcelSheet excelSheet = this.getExcelReadService().readSheet(myWorkBook, sheetName,
					sheetCellTypeList);
			
			List<SheetRow> sortedSheetRowList = this.getUtilExcelService().sortSheetRowList(
					excelSheet.getSheetRowList(), columnIndexToSort, columnIndexToFilter, tokenToFilter);


			if (this.getUtilExcelService().didSheetSort(sortedSheetRowList)) {
				this.updateRowNumber(sortedSheetRowList);
				
				this.getExcelWriteService().deleteSheet(myWorkBook, sheetName);
				this.getExcelWriteService().addSheetToExcel(myWorkBook, sheetName, 0, sheetCellTypeList, sortedSheetRowList);
				return new WorkbookResponse(myWorkBook, true, true);
			}
			else {
				return new WorkbookResponse(myWorkBook, false, true);
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
			return new WorkbookResponse(null, false, false);
		} 
	}
	
	private void updateRowNumber(List<SheetRow> sortedSheetRowList) {
		int rowNumber = 0; //Header
		for (SheetRow sheetRow: sortedSheetRowList) {
			rowNumber++;
			sheetRow.setRowNumber(rowNumber);
		}
		this.getUtilExcelService().updateCellIdValue(sortedSheetRowList);
	}


	public UtilDateService getUtilDateService() {
		return utilDateService;
	}

	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}

	public ExcelReadService getExcelReadService() {
		return excelReadService;
	}

	public void setExcelReadService(ExcelReadService excelReadService) {
		this.excelReadService = excelReadService;
	}

	public ExcelWriteService getExcelWriteService() {
		return excelWriteService;
	}

	public void setExcelWriteService(ExcelWriteService excelWriteService) {
		this.excelWriteService = excelWriteService;
	}

	public UtilExcelService getUtilExcelService() {
		return utilExcelService;
	}

	public void setUtilExcelService(UtilExcelService utilExcelService) {
		this.utilExcelService = utilExcelService;
	}
}
