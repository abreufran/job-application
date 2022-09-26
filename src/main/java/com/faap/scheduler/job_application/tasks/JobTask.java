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
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.enums.Flag;

public class JobTask extends TimerTask {
	public static int NUMBER_OF_CELLS = 7;
	
	private Task task;

	public JobTask(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		System.out.println("Job Task: " + LocalDateTime.now());
		try {
			this.readExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readExcel() throws IOException {
		File myFile = new File("G://My Drive/Things to do.xlsx");
		//File myFile = new File("C://Things to do.xlsx");
		FileInputStream fis = new FileInputStream(myFile);

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

		// Return first sheet from the XLSX workbook
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = mySheet.iterator();

		List<String> cellList = null;
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

		int count = -1;
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			count++;
			if (count > 0) {
				cellList = new ArrayList<>();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case STRING:
						cellList.add(cell.getStringCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							cellList.add(dateFormat.format(cell.getDateCellValue()));
						} else {
							cellList.add(String.valueOf(cell.getNumericCellValue()));
						}
						break;
					case FORMULA:
						cellList.add(cell.getRichStringCellValue().toString());
						break;
					default:
						cellList.add(null);
					}
				}
				//Sí la celda ID no está vacía
				if (cellList.size() > 0 && cellList.get(0) != null) {
					//Si la cantidad de celdas por fila es la correcta
					if (cellList.size() == NUMBER_OF_CELLS) {
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
					} else {
						System.out.print("ERROR: \t" + cellList.size() + "\t");
						cellList.stream().forEach(c -> System.out.print(c + "\t"));
						System.out.println("");
					}

				}

			}
		}
		fis.close();
		myWorkBook.close();
		myFile = null;
		fis = null;
	}

	public void saveJob(String job) {
		task.saveData(job, Flag.JOB);
	}

}
