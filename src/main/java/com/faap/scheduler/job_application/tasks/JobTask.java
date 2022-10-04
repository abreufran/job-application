package com.faap.scheduler.job_application.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.models.job.JobSheet;
import com.faap.scheduler.job_application.models.job.SheetRow;
import com.faap.scheduler.job_application.models.job.SheetType;

public class JobTask extends TimerTask {
	public static int NUMBER_OF_CELLS = 7;
	public static String JOB_FILE_NAME = "G://My Drive/Things to do.xlsx";
	
	private Task task;

	public JobTask(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		System.out.println("Job Task: " + LocalDateTime.now());
		try {
			this.readAndSaveExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fillEmptyFields() throws IOException {
		XSSFWorkbook myWorkBook = this.readExcel();
		
		JobSheet jobSheet = this.readSheet(myWorkBook);
		
		//TODO CONTINUAR AQUIIII
		
		myWorkBook.close();
	}

	public void readAndSaveExcel() throws IOException {
		XSSFWorkbook myWorkBook = this.readExcel();
		
		JobSheet jobSheet = this.readSheet(myWorkBook);
		
		for(SheetRow sheetRow: jobSheet.getSheetRowList()) {
			List<String> cellList = this.getStrCellList(sheetRow.getCellList());
			
			StringBuffer cellStr = new StringBuffer();
			for (String c : cellList) {
				if (cellStr.length() > 0) {
					cellStr.append(",");
				}
				if (c != null) {
					cellStr.append(c.replaceAll(",", "__"));
				} else {
					cellStr.append("");
				}

			}
			this.saveJob(cellStr.toString());
		}

		myWorkBook.close();
	}
	
	private JobSheet readSheet(XSSFWorkbook myWorkBook) {
		JobSheet jobSheet = new JobSheet(SheetType.THINGS_TO_DO);
		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();
		
		List<Cell> cellList = null;
		int x = -1;
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			x++;
			if (x > 0) {
				cellList = new ArrayList<>();
				
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {					
					cellList.add(cellIterator.next());
				}
			}
			//Sí la celda ID no está vacía
			if (cellList.size() > 0 && this.validCell(cellList.get(0))) {
				//Si la cantidad de celdas por fila es la correcta
				if (cellList.size() == NUMBER_OF_CELLS) {
					jobSheet.getSheetRowList().add(new SheetRow(cellList));
				}
				else {
					List<String> strCellList = this.getStrCellList(cellList);
					System.out.print("ERROR: \t" + strCellList.size() + "\t");
					strCellList.stream().forEach(c -> System.out.print(c + "\t"));
					System.out.println("");
				}
			}
			
		}
		
		return jobSheet;
	}
	
	private List<String> getStrCellList(List<Cell> cellList) {
		List<String> strCellList = new ArrayList<>();
		
		for(Cell cell: cellList) {
			strCellList.add(this.readCell(cell));
		}
		return strCellList;
	}
	
	private String readCell(Cell cell) {
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
		
	private boolean validCell(Cell cell) {
		return cell.getCellType() == CellType.STRING || 
				cell.getCellType() == CellType.NUMERIC ||
				cell.getCellType() == CellType.FORMULA;
	}
	
	private XSSFWorkbook readExcel() throws IOException {
		File myFile = new File(JOB_FILE_NAME);

		FileInputStream fis = new FileInputStream(myFile);
		
		fis.close();
		myFile = null;
		fis = null;
		// Finds the workbook instance for XLSX file
		return new XSSFWorkbook(fis);
	}

	public void saveJob(String job) {
		task.saveData(job, Flag.JOB);
	}

}
