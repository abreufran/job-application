package com.faap.scheduler.job_application.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Job  extends TimerTask {
	
	@Override
	public void run() {
		System.out.println(LocalDateTime.now());
	}

	public void readExcel() throws IOException {
		File myFile = new File("G://My Drive/Test.xlsx");
		FileInputStream fis = new FileInputStream(myFile);

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		
		List<String> cellList = null;

		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			cellList = new ArrayList<>();

			// For each row, iterate through each columns
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case STRING:
					cellList.add(cell.getStringCellValue() + "\t");
					break;
				case NUMERIC:
					cellList.add(cell.getNumericCellValue() + "\t");
					break;
				case BOOLEAN:
					cellList.add(cell.getBooleanCellValue() + "\t");
					break;
				default:
				}
			}
			if(cellList.size() > 0) {
				cellList.stream().forEach(c -> System.out.print(c));
				System.out.println("");
			}
			
		}
		
		myWorkBook.close();
	}

}
