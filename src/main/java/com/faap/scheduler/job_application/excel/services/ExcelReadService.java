package com.faap.scheduler.job_application.excel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.models.thing_to_do.ValidCellListResponse;

public class ExcelReadService {
	private UtilDateService utilDateService;
	private UtilExcelService utilExcelService;

	public ExcelReadService(UtilDateService utilDateService, UtilExcelService utilExcelService) {
		this.setUtilDateService(utilDateService);
		this.setUtilExcelService(utilExcelService);
	}

	protected SheetWrapper readSheet(XSSFWorkbook myWorkBook, String sheetName, List<CellTypeWrapper> wrapperCellTypeList)
			throws Exception {

		SheetWrapper sheetWrapper = new SheetWrapper(sheetName);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();


		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			this.utilExcelService.completeRow(row, myWorkBook, wrapperCellTypeList);

			List<Cell> cellList = this.utilExcelService.getCellList(row, wrapperCellTypeList);

			ValidCellListResponse validCellListResponse = this.validCellList(cellList, row.getRowNum(), wrapperCellTypeList);

			if (validCellListResponse == ValidCellListResponse.OK) {
				if (row.getRowNum() > 0) {
					this.addSheetRow(sheetWrapper, cellList, wrapperCellTypeList, row);
				}

			} else {
				if (validCellListResponse == ValidCellListResponse.INVALID_HEADER_CELL_LIST) {
					System.out.println("ERROR: Row Number: " + (row.getRowNum() + 1) + " / INVALID_HEADER_CELL_LIST");
					throw new Exception("INVALID_HEADER_CELL_LIST");
				}
				if (validCellListResponse == ValidCellListResponse.INVALID_REQUIRED_CELL) {
					System.out.println("WARNING: Row Number: " + (row.getRowNum() + 1) + " / INVALID_REQUIRED_CELL. DISCARTED ROW.");
				}
			}

		}

		this.checkDateCell(myWorkBook, sheetWrapper.getSheetRowList());

		return sheetWrapper;
	}
	
	private void checkDateCell(XSSFWorkbook myWorkBook, List<RowWrapper> wrapperRowList) {
		for (RowWrapper rowWrapper : wrapperRowList) {
			List<CellWrapper> sheetDateCells = rowWrapper.getSheetCellList().stream()
					.filter(sc -> sc.getSheetCellType().isDate()).collect(Collectors.toList());
			
			for (CellWrapper cellWrapper : sheetDateCells) {
				if (cellWrapper.getSheetCellType().getCellType() != CellType.NUMERIC || !DateUtil.isCellDateFormatted(cellWrapper.getCell())) {

					if (cellWrapper.getSheetCellType().isRequired()) {
						System.out.println("WARNING: RowNumber: " + (rowWrapper.getRowNumber() + 1) 
								+ " / Required Date columnIdex: " + cellWrapper.getSheetCellType().getColumnIndex() + " / Fixed.");
						cellWrapper.setCellValue(this.utilDateService.getStrDate(LocalDate.now()));
					}

				}
			}
		}
	}
	
	public List<CellWrapper> getSheetCellList(List<Cell> cellList, List<CellTypeWrapper> wrapperCellTypeList,
			int rowNumber) {
		List<CellWrapper> wrapperCellList = new ArrayList<>();
		for (Cell cell: cellList) {
			
			CellTypeWrapper cellTypeWrapper = wrapperCellTypeList.stream().filter(ct -> ct.getColumnIndex() == cell.getColumnIndex()).findFirst().orElse(null);
					
			wrapperCellList.add(new CellWrapper(
					cellTypeWrapper, 
					this.utilExcelService.readCell(cell), 
					cell));
		}
		return wrapperCellList;
	}
	
	private ValidCellListResponse validCellList(List<Cell> cellList, int rowNumber, List<CellTypeWrapper> sheeCellTypeList) {
		List<Cell> requiredCellList = cellList.stream().filter(c -> {
			return sheeCellTypeList.stream().anyMatch(ct -> ct.getColumnIndex() == c.getColumnIndex() && ct.isRequired());
		}).collect(Collectors.toList());
		
		if (!this.validCellList(requiredCellList)) {
			return ValidCellListResponse.INVALID_REQUIRED_CELL;
		}
		if (rowNumber == 0 && !this.validHeaderCellList(cellList, sheeCellTypeList)) {
			return ValidCellListResponse.INVALID_HEADER_CELL_LIST;
		}
		return ValidCellListResponse.OK;
	}
	
	private boolean validCellList(List<Cell> cellList) {
		for(Cell cell: cellList) {
			if(this.validCell(cell)) {
				String cellValue = this.utilExcelService.readCell(cell);
				if(cellValue != null && !cellValue.trim().equals("")) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || cell.getCellType() == CellType.NUMERIC
				|| cell.getCellType() == CellType.FORMULA;
	}
	
	private boolean validHeaderCellList(List<Cell> cellList, List<CellTypeWrapper> wrapperCellTypeList) {
		List<String> strCellList = this.getStrCellList(cellList);

		return wrapperCellTypeList.stream().allMatch(sct -> {
			boolean match = strCellList.stream().filter(sc -> sct.getName().equals(sc)).findFirst().isPresent();
			if(!match) {
				System.out.println("Header Column (" + sct.getName() + "): " + match + " Do Not Match");
			}
			return match;
		});

	}
	
	public RowWrapper addSheetRow(SheetWrapper sheetWrapper, List<Cell> cellList, List<CellTypeWrapper> wrapperCellTypeList, Row row) {
		System.out.println("INFO: Row Number: " + (row.getRowNum() + 1) + " / Reading");
		List<CellWrapper> wrapperCellList = this.getSheetCellList(cellList,
				wrapperCellTypeList, row.getRowNum());
		RowWrapper rowWrapper = new RowWrapper(wrapperCellList, row.getRowNum());
		sheetWrapper.getSheetRowList().add(rowWrapper);
		
		return rowWrapper;
	}
	
	private List<String> getStrCellList(List<Cell> cellList) {
		List<String> strCellList = new ArrayList<>();

		for (Cell cell : cellList) {
			strCellList.add(this.utilExcelService.readCell(cell));
		}
		return strCellList;
	}
	
    
    protected XSSFWorkbook readExcel(String filePath) throws IOException {
		File myFile = new File(filePath);

		FileInputStream fis = new FileInputStream(myFile);
		ZipSecureFile.setMinInflateRatio(-1.0d);
		XSSFWorkbook myWorkbook = new XSSFWorkbook(fis);

		fis.close();
		myFile = null;
		fis = null;

		return myWorkbook;
	}

	public void setUtilDateService(UtilDateService utilDateService) {
		this.utilDateService = utilDateService;
	}

	public void setUtilExcelService(UtilExcelService utilExcelService) {
		this.utilExcelService = utilExcelService;
	}
}
