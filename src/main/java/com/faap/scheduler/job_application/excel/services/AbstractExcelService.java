package com.faap.scheduler.job_application.excel.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.excel.dtos.FieldEmptyFieldsRequest;
import com.faap.scheduler.job_application.excel.dtos.FieldEmptyFieldsResponse;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetType;
import com.faap.scheduler.job_application.models.job.SheetCell;
import com.faap.scheduler.job_application.models.job.SheetCellType;
import com.faap.scheduler.job_application.models.job.SheetRow;
import com.faap.scheduler.job_application.models.job.ValidCellListResponse;

public abstract class AbstractExcelService {
	
	public abstract void completeSheetCellList(List<SheetCell> sheetCellList, XSSFWorkbook myWorkBook);

	public XSSFWorkbook readExcel(String filePath) throws IOException {
		File myFile = new File(filePath);

		FileInputStream fis = new FileInputStream(myFile);
		XSSFWorkbook myWorkbook = new XSSFWorkbook(fis);
		
		
		fis.close();
		myFile = null;
		fis = null;

		return myWorkbook;
	}	
	
	protected void writeExcel(XSSFWorkbook myWorkBook, String filePath) throws IOException {
		File myFile = new File(filePath);
		
		FileOutputStream outputStream = new FileOutputStream(myFile);
		
		myWorkBook.write(outputStream);
		
		outputStream.close();
		myFile = null;
		outputStream = null;	
	}
	
	public FieldEmptyFieldsResponse fillEmptyFields(FieldEmptyFieldsRequest req) {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.readExcel(req.getFilePath());
			
			ExcelSheet excelSheet = this.readSheet(myWorkBook, req.getSheetType(), 
					req.getNumberOfCells(), req.getRequiredCellNumber());
			
			List<SheetRow> incompleteSheetRowList = this.calculateIncompleteSheetRowList(excelSheet.getSheetRowList());
			
			List<SheetCell> incompleteSheetCellList = this.calculateIncompleteSheetCell(incompleteSheetRowList);
			
			this.completeSheetCellList(incompleteSheetCellList, myWorkBook);
			
			//boolean sorted = this.sortSheet2(myWorkBook.getSheetAt(0), 4, 1, jobSheet.getSheetRowList().size());
			
			if(incompleteSheetCellList.size() > 0 /*|| sorted*/) {
				System.out.println("Saving WorkBook");
				this.writeExcel(myWorkBook, req.getFilePath());
			}
			
			return new FieldEmptyFieldsResponse(true);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new FieldEmptyFieldsResponse(false);
		}
		finally {
			if(myWorkBook != null) {
				try {
					myWorkBook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected List<SheetRow> calculateIncompleteSheetRowList(List<SheetRow> sheetRowList) {
		return sheetRowList.stream().filter(sr -> this.incompleteSheetRow(sr)).collect(Collectors.toList());
	}
	
	private boolean incompleteSheetRow(SheetRow sheetRow) {
		return sheetRow.getSheetCellList().stream().anyMatch(sc -> this.incompleteSheetCell(sc));
	}
	
	private boolean incompleteSheetCell(SheetCell sheetCell) {
		String strCell = this.readCell(sheetCell.getCell());
		return sheetCell.getSheetCellType().isRequired() && (strCell == null || strCell.trim().equals(""));
	}
	
	private List<SheetCell> calculateIncompleteSheetCell(List<SheetRow> incompleteSheetRowList) {
		List<SheetCell> incompleteSheetCellList = new ArrayList<>();
		for(SheetRow sheetRow: incompleteSheetRowList) {
			List<SheetCell> sheetCellList = sheetRow.getSheetCellList().stream().filter(sc -> this.incompleteSheetCell(sc)).collect(Collectors.toList());
			incompleteSheetCellList.addAll(sheetCellList);
		}
		return incompleteSheetCellList;
	}
	
	public ExcelSheet readSheet(XSSFWorkbook myWorkBook, 
			SheetType sheetType, 
			int numberOfCells, 
			int requiredCellNumber) throws Exception {
		
		ExcelSheet excelSheet = new ExcelSheet(sheetType);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(sheetType.getSheetNumber());

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		
		int rowNumber = 0;
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			this.completeRow(row, myWorkBook, numberOfCells, excelSheet.getSheetCellTypeHashMap());
			rowNumber++;
			Iterator<Cell> cellIterator = row.cellIterator();
			
			List<Cell> cellList = new ArrayList<>();
			
			// For each row, iterate through each columns
			while (cellIterator.hasNext()) {	
				cellList.add(cellIterator.next());
			}
			
			ValidCellListResponse validCellListResponse = this.validCellList(cellList, rowNumber, numberOfCells, requiredCellNumber);
			if(validCellListResponse == ValidCellListResponse.INVALID_CELLS_NUMBER) {
				
			}
			
			if(validCellListResponse == ValidCellListResponse.OK) {
				if(rowNumber == 1) {
					for(int i = 0; i < cellList.size(); i++) {
						excelSheet.getSheetCellTypeHashMap().put(i, this.getSheetCellType(cellList.get(i)));
					}
				}
				else {
					List<SheetCell> sheetCellList = this.getSheetCellList(cellList, excelSheet.getSheetCellTypeHashMap(), rowNumber);
					excelSheet.getSheetRowList().add(new SheetRow(sheetCellList, rowNumber));
				}
				
			}
			else {
				if(validCellListResponse == ValidCellListResponse.INVALID_CELLS_NUMBER) {
					List<String> strCellList = this.getStrCellList(cellList);
					System.out.print("ROW: " + rowNumber + " ERROR: \t" + strCellList.size() + "\t");
					strCellList.stream().forEach(c -> System.out.print(c + "\t"));
					System.out.println("");
					throw new Exception("INVALID_CELLS_NUMBER");
				}
				if(validCellListResponse == ValidCellListResponse.INVALID_HEADER_CELL_LIST) {
					System.out.print("ERROR: INVALID_HEADER_CELL_LIST");
					throw new Exception("INVALID_HEADER_CELL_LIST");
				}
			}

			
		}
		
		return excelSheet;
	}
	
	private SheetCellType getSheetCellType(Cell cell) {
		return Arrays.stream(SheetCellType.values()).filter(sct -> sct.getName().equals(this.readCell(cell))).findFirst().orElse(null);
	}
	
	private List<SheetCell> getSheetCellList(List<Cell> cellList, Map<Integer,SheetCellType> sheetCellTypeHashMap, int rowNumber) {
		List<SheetCell> sheetCellList = new ArrayList<>();
		for(int i = 0; i < cellList.size(); i++) {
			sheetCellList.add(new SheetCell(sheetCellTypeHashMap.get(i), cellList.get(i), rowNumber));
		}
		return sheetCellList;
	}
	
	private void completeRow(Row row, XSSFWorkbook myWorkBook, int numberOfCells, Map<Integer,SheetCellType> sheetCellTypeHashMap) {
		List<Cell> cellList = new ArrayList<>();
		Iterator<Cell> cellIterator = row.cellIterator();
		// For each row, iterate through each columns
		while (cellIterator.hasNext()) {	
			cellList.add(cellIterator.next());
		}
		
		for(int i = 0; i < numberOfCells; i++) {
			if(this.existColumnIndex(cellList, i)) {
				CellType cellType = sheetCellTypeHashMap.get(i).getCellType();
				Cell cell = row.createCell(i, cellType);
				cell.setBlank();
				CellStyle style = myWorkBook.createCellStyle();
				style.setAlignment(HorizontalAlignment.CENTER);
				
				
				if(sheetCellTypeHashMap.get(i).isDate()) {
					CreationHelper createHelper = myWorkBook.getCreationHelper();
					style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));	
				}
				cell.setCellStyle(style);
			}
		}
	}
	
	private boolean existColumnIndex(List<Cell> cellList, int columNumber) {
		return !cellList.stream().anyMatch(c -> c.getColumnIndex() == columNumber);
	}
	
	private ValidCellListResponse validCellList(List<Cell> cellList, int rowNumber, int numberOfCells, int requiredCellNumber) {
		if(cellList.size() == 0) {
			return ValidCellListResponse.EMPTY_CELL_LIST;
		}
		if (cellList.size() != numberOfCells) {
			return ValidCellListResponse.INVALID_CELLS_NUMBER;
		}
		if(!this.validCell(cellList.get(requiredCellNumber))) {
			return ValidCellListResponse.INVALID_REQUIRED_CELL;
		}
		if(rowNumber == 1 && !this.validHeaderCellList(cellList)) {
			return ValidCellListResponse.INVALID_HEADER_CELL_LIST;
		}
		return ValidCellListResponse.OK;
	}
	
	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || 
				cell.getCellType() == CellType.NUMERIC ||
				cell.getCellType() == CellType.FORMULA;
	}
	
	private boolean validHeaderCellList(List<Cell> cellList) {
		List<String> strCellList = this.getStrCellList(cellList);
		
		return Arrays.stream(SheetCellType.values()).allMatch(sct -> {
			boolean match = strCellList.stream().filter(sc -> sct.getName().equals(sc)).findFirst().isPresent();
			System.out.println(sct.getName() + ":" + match);
			return match;
		});

	}
	
	private List<String> getStrCellList(List<Cell> cellList) {
		List<String> strCellList = new ArrayList<>();
		
		for(Cell cell: cellList) {
			strCellList.add(this.readCell(cell));
		}
		return strCellList;
	}
	
	public String readCell(Cell cell) {
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return dateFormat.format(cell.getDateCellValue());
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case FORMULA:
			return cell.getRichStringCellValue().toString();
		default:
			return null;
		}
	}
	
//	public boolean sortSheet2(XSSFSheet sheet, int column, int rowStart, int lastRow) {
//		boolean sorted = false;
//		boolean sorting = true;
//		
//		//List<Integer> integerRowListSorted = this.getIntegerRowListSorted(sheet);
//		while (sorting == true) {
//			int finalRowToSort = this.getFinalRowToSort(sheet, column, rowStart, lastRow);
//			if(finalRowToSort != -1) {
//			
//				int initilRowToSort = finalRowToSort - 1;
//				
//				sheet.shiftRows(initilRowToSort, initilRowToSort, lastRow - initilRowToSort + 1);
//				sheet.shiftRows(finalRowToSort, finalRowToSort, -1);
//				sheet.shiftRows(lastRow + 1, lastRow + 1, (lastRow - initilRowToSort) * -1);
//				sorted = true;
//			}
//			else {
//				sorting = false;
//			}
//		}
//		
//		return sorted;
//	}
//	
//	private int getFinalRowToSort(XSSFSheet sheet, int column, int rowStart, int lastRow) {
//		List<Row> rowList = new ArrayList<>();
//		for (Row row : sheet) {
//			//if(row.getCell(7).getRichStringCellValue().toString().equals("PENDING")) {
//				rowList.add(row);
//			//}
//		}
//		
//		for (Row row : rowList) {
//            // skip if this row is before first to sort
//            if (row.getRowNum()<rowStart) continue;
//            // end if this is last row
//            if (lastRow==row.getRowNum()) break;
//            Row row2 = sheet.getRow(row.getRowNum()+1);
//            if (row2 == null) continue;
//            String firstValue = (row.getCell(column) != null) ? row.getCell(column).getStringCellValue() : "";
//            String secondValue = (row2.getCell(column) != null) ? row2.getCell(column).getStringCellValue() : "";
//            //compare cell from current row and next row - and switch if secondValue should be before first
//            if (secondValue.compareToIgnoreCase(firstValue)<0) {
//            	return row.getRowNum()+1;
//            }
//		}
//		return -1;
//	}
}
