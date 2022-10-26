package com.faap.scheduler.job_application.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.faap.scheduler.job_application.enums.Flag;
import com.faap.scheduler.job_application.excel.models.SheetWrapper;
import com.faap.scheduler.job_application.excel.models.PeriodicTaskColumnType;
import com.faap.scheduler.job_application.excel.models.CellWrapper;
import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.services.JobExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;

public class JobTask extends TimerTask {
	public static String BACKUP_PATH = "C://Users/Administrator/Desktop/job_backup";
	public static String JOB_FILE_NAME = "G://My Drive/Things_to_do.xlsx";
	public static String SHEET_NAME = "Things to do";
	public static int NUMBER_OF_CELLS = 8;
	public static int REQUIRED_CELL_NUMBER = 5;
	public static List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";

	
	private FileBackupRepository fileBackupRepository;
	private DataFileRepository dataFileRepository;
	private JobExcelService jobExcelService;
	private UtilExcelService utilExcelService;
	private UtilFileService utilFileService;

	public JobTask(DataFileRepository dataFileRepository, JobExcelService jobExcelService, 
			UtilExcelService utilExcelService,
			UtilFileService utilFileService, FileBackupRepository fileBackupRepository) {
		this.dataFileRepository = dataFileRepository;
		this.setJobExcelService(jobExcelService);
		this.setUtilExcelService(utilExcelService);
		this.setUtilFileService(utilFileService);
		this.setFileBackupRepository(fileBackupRepository);
	}

	@Override
	public void run() {
		System.out.println("Job Task: " + LocalDateTime.now());
		try {
			if(this.didOriginFileChange() && this.makeBackup()) {
				List<CellTypeWrapper> initialSheetCellTypeList = new ArrayList<>();
		    	
		    	for(PeriodicTaskColumnType periodicTask: PeriodicTaskColumnType.values()) {
		    		initialSheetCellTypeList.add(new CellTypeWrapper(periodicTask));
		    	}
		    	
		    	List<CellTypeWrapper> finalSheetCellTypeList = new ArrayList<>();
		    	
		    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
		    		finalSheetCellTypeList.add(new CellTypeWrapper(thingToDoColumnType));
		    	}
				
				//this.jobExcelService.fillSortAndSaveSheet(JOB_FILE_NAME, JOB_FILE_NAME, SHEET_NAME, wrapperCellTypeList,
				//		COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
				
				this.jobExcelService.loadAndSortThingsToDoSheet(jobExcelService, JOB_FILE_NAME, JOB_FILE_NAME, initialSheetCellTypeList, finalSheetCellTypeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readAndSaveExcel() throws Exception {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.jobExcelService.readExcel(JOB_FILE_NAME);
			
			List<CellTypeWrapper> wrapperCellTypeList = new ArrayList<>();
	    	
	    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
	    		wrapperCellTypeList.add(new CellTypeWrapper(thingToDoColumnType));
	    	}
			
			SheetWrapper jobSheet = this.jobExcelService.readSheet(myWorkBook,SHEET_NAME, wrapperCellTypeList);
			
			for(RowWrapper rowWrapper: jobSheet.getSheetRowList()) {
				List<String> cellList = this.getStrCellListFromSheetCellList(rowWrapper.getSheetCellList());
				
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
	
	private List<String> getStrCellListFromSheetCellList(List<CellWrapper> wrapperCellList) {
		List<String> strCellList = new ArrayList<>();
		
		for(CellWrapper cellWrapper: wrapperCellList) {
			strCellList.add(this.utilExcelService.readCell(cellWrapper.getCell()));
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
		String destinationPath = this.utilFileService.makeBackup(BACKUP_PATH, JOB_FILE_NAME);
		if(destinationPath != null) {
			LocalDateTime originFileDateModified = this.utilFileService.getDateModified(JOB_FILE_NAME);
			this.fileBackupRepository.saveFileBackup(destinationPath, originFileDateModified, Flag.JOB);
			return true;
		}
		return false;
	}

	public UtilExcelService getUtilExcelService() {
		return utilExcelService;
	}

	public void setUtilExcelService(UtilExcelService utilExcelService) {
		this.utilExcelService = utilExcelService;
	}

}
