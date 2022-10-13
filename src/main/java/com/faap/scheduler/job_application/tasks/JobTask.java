package com.faap.scheduler.job_application.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.excel.models.ExcelSheet;
import com.faap.scheduler.job_application.excel.models.SheetCell;
import com.faap.scheduler.job_application.excel.models.SheetCellType;
import com.faap.scheduler.job_application.excel.models.SheetRow;
import com.faap.scheduler.job_application.excel.services.JobExcelService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;

public class JobTask extends TimerTask {
	public static String BACKUP_PATH = "C://Users/Administrator/Desktop/job_backup";
	public static String JOB_FILE_NAME = "G://My Drive/Things_to_do.xlsx";
	public static String SHEET_NAME = "Things to do";
	public static int NUMBER_OF_CELLS = 8;
	public static int REQUIRED_CELL_NUMBER = 5;
	public static int COLUMN_INDEX_TO_SORT = 4;
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";
	public static List<SheetCellType> SHEET_CELL_TYPE_LIST = 
			Arrays.asList(SheetCellType.ID, SheetCellType.INCIDENCE_DATE, 
					SheetCellType.EXECUTION_DATE, SheetCellType.ESTIMATED_DATE,
					SheetCellType.PRIORITY, SheetCellType.THINGS_TO_DO,
					SheetCellType.CATEGORY, SheetCellType.STATUS);
	
	private FileBackupRepository fileBackupRepository;
	private DataFileRepository dataFileRepository;
	private JobExcelService jobExcelService;
	private UtilFileService utilFileService;

	public JobTask(DataFileRepository dataFileRepository, JobExcelService jobExcelService, 
			UtilFileService utilFileService, FileBackupRepository fileBackupRepository) {
		this.dataFileRepository = dataFileRepository;
		this.setJobExcelService(jobExcelService);
		this.setUtilFileService(utilFileService);
		this.setFileBackupRepository(fileBackupRepository);
	}

	@Override
	public void run() {
		System.out.println("Job Task: " + LocalDateTime.now());
		try {
			if(this.didOriginFileChange() && this.makeBackup()) {
				//this.readAndSaveExcel();
				
				//this.jobExcelService.fillAndSaveEmptyFields(JOB_FILE_NAME, SHEET_NAME, SHEET_CELL_TYPE_LIST);
				
				//this.jobExcelService.rebuildSheet(JOB_FILE_NAME, SHEET_NAME, SHEET_CELL_TYPE_LIST);
				
				this.jobExcelService.fillSortAndSaveSheet(JOB_FILE_NAME, JOB_FILE_NAME, SHEET_NAME, SHEET_CELL_TYPE_LIST,
						COLUMN_INDEX_TO_SORT, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
				
				//this.jobExcelService.setFilter(JOB_FILE_NAME, SHEET_NAME, SHEET_CELL_TYPE_LIST,
				//		COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readAndSaveExcel() throws Exception {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.jobExcelService.getExcelReadService().readExcel(JOB_FILE_NAME);
			
			ExcelSheet jobSheet = this.jobExcelService.getExcelReadService().readSheet(myWorkBook,SHEET_NAME, SHEET_CELL_TYPE_LIST);
			
			for(SheetRow sheetRow: jobSheet.getSheetRowList()) {
				List<String> cellList = this.getStrCellListFromSheetCellList(sheetRow.getSheetCellList());
				
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
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(myWorkBook != null) {
				myWorkBook.close();
			}
		}
		
	}
	
	private List<String> getStrCellListFromSheetCellList(List<SheetCell> sheetCellList) {
		List<String> strCellList = new ArrayList<>();
		
		for(SheetCell sheetCell: sheetCellList) {
			strCellList.add(this.jobExcelService.getUtilExcelService().readCell(sheetCell.getCell()));
		}
		return strCellList;
	}
	
	public void saveJob(String job) {
		dataFileRepository.saveDataFile(job, Flag.JOB);
	}

	public JobExcelService getJobExcelService() {
		return jobExcelService;
	}

	public void setJobExcelService(JobExcelService jobExcelService) {
		this.jobExcelService = jobExcelService;
	}

	public UtilFileService getUtilFileService() {
		return utilFileService;
	}

	public void setUtilFileService(UtilFileService utilFileService) {
		this.utilFileService = utilFileService;
	}

	public FileBackupRepository getFileBackupRepository() {
		return fileBackupRepository;
	}

	public void setFileBackupRepository(FileBackupRepository fileBackupRepository) {
		this.fileBackupRepository = fileBackupRepository;
	}
	
	public boolean didOriginFileChange() {
		return !this.fileBackupRepository.existFileBackup(this.utilFileService.getDateModified(JOB_FILE_NAME));
	}
	
	public boolean makeBackup() {
		System.out.println("Making backup: " + JOB_FILE_NAME);
		String destinationPath = BACKUP_PATH + this.utilFileService.getFileNameBackup(JOB_FILE_NAME);
		boolean copied = this.utilFileService.copy(JOB_FILE_NAME, destinationPath);
		if(copied) {
			LocalDateTime originFileDateModified = this.utilFileService.getDateModified(JOB_FILE_NAME);
			this.fileBackupRepository.saveFileBackup(destinationPath, originFileDateModified, Flag.JOB);
		}
		return copied;
	}

}
