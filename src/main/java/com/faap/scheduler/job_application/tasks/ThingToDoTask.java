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
import com.faap.scheduler.job_application.excel.services.ThingToDoExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.models.thing_to_do.ThingToDo;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;

public class ThingToDoTask extends TimerTask {
	public static String SHEET_NAME = "Things to do";
	public static int NUMBER_OF_CELLS = 8;
	public static int REQUIRED_CELL_NUMBER = 5;
	public static List<Integer> COLUMN_INDEX_TO_SORT_LIST = Arrays.asList(4, 3);
	public static int COLUMN_INDEX_TO_FILTER = 7;
	public static String TOKEN_TO_FILTER = "PENDING";

	private String backupFileName;
	private String initialThingToDoFileName;
	private String finalThingToDoFileName;
	private FileBackupRepository fileBackupRepository;
	private DataFileRepository dataFileRepository;
	private ThingToDoExcelService thingToDoExcelService;
	private UtilExcelService utilExcelService;
	private UtilFileService utilFileService;

	public ThingToDoTask(DataFileRepository dataFileRepository, 
			ThingToDoExcelService thingToDoExcelService, 
			UtilExcelService utilExcelService,
			UtilFileService utilFileService, 
			FileBackupRepository fileBackupRepository,
			String backupPath,
			String initialThingToDoFileName,
			String finalThingToDoFileName) {
		
		this.dataFileRepository = dataFileRepository;
		this.setThingToDoExcelService(thingToDoExcelService);
		this.setUtilExcelService(utilExcelService);
		this.setUtilFileService(utilFileService);
		this.setFileBackupRepository(fileBackupRepository);
		this.setBackupFileName(backupPath);
		this.setInitialThingToDoFileName(initialThingToDoFileName);
		this.setFinalThingToDoFileName(finalThingToDoFileName);
	}

	@Override
	public void run() {
		System.out.println("Thing To Do Task: " + LocalDateTime.now());
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
				
				//this.thingToDoExcelService.fillSortAndSaveSheet(THING_TO_DO_FILE_NAME, THING_TO_DO_FILE_NAME, SHEET_NAME, wrapperCellTypeList,
				//		COLUMN_INDEX_TO_SORT_LIST, COLUMN_INDEX_TO_FILTER, TOKEN_TO_FILTER);
				
				this.thingToDoExcelService.loadAndSortThingsToDoSheet(thingToDoExcelService, this.initialThingToDoFileName, this.finalThingToDoFileName, initialSheetCellTypeList, finalSheetCellTypeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void importThingsToDoData() throws Exception {
		XSSFWorkbook myWorkBook = null;
		try {
			
			List<ThingToDo> reactThingToDoList = this.getReactThingToDoList();
			
			if(reactThingToDoList.size() > 0) {
			
				myWorkBook = this.thingToDoExcelService.readExcel(this.initialThingToDoFileName);
				
				List<CellTypeWrapper> cellTypeWrapperList = new ArrayList<>();
		    	
		    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
		    		cellTypeWrapperList.add(new CellTypeWrapper(thingToDoColumnType));
		    	}
				
				SheetWrapper sheetWrapper = this.thingToDoExcelService.readSheet(myWorkBook,SHEET_NAME, cellTypeWrapperList);
				
				List<CellWrapper> reactCellWrapperList = this.utilExcelService.getCellWrapperLit(reactThingToDoList, cellTypeWrapperList);
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
	
	public void readAndExportThingsToDoSheet() throws Exception {
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = this.thingToDoExcelService.readExcel(this.initialThingToDoFileName);
			
			List<CellTypeWrapper> cellTypeWrapperList = new ArrayList<>();
	    	
	    	for(ThingToDoColumnType thingToDoColumnType: ThingToDoColumnType.values()) {
	    		cellTypeWrapperList.add(new CellTypeWrapper(thingToDoColumnType));
	    	}
			
			SheetWrapper sheetWrapper = this.thingToDoExcelService.readSheet(myWorkBook,SHEET_NAME, cellTypeWrapperList);
			
			for(RowWrapper rowWrapper: sheetWrapper.getRowWrapperList()) {
				List<String> cellList = this.getStrCellListFromSheetCellList(rowWrapper.getCellWrapperList());
				
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
				this.saveThingToDo(cellStr.toString());
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
	
	public void saveThingToDo(String thingToDo) {
		dataFileRepository.saveDataFile(thingToDo, Flag.THING_TO_DO);
	}
	
	public List<ThingToDo> getReactThingToDoList() {
		return dataFileRepository.readReactThingToDo();
	}

	public ThingToDoExcelService getThingToDoExcelService() {
		return thingToDoExcelService;
	}

	public void setThingToDoExcelService(ThingToDoExcelService thingToDoExcelService) {
		this.thingToDoExcelService = thingToDoExcelService;
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
		return !this.fileBackupRepository.existFileBackup(this.utilFileService.getDateModified(this.initialThingToDoFileName));
	}
	
	public boolean makeBackup() {
		String destinationPath = this.utilFileService.makeBackup(this.backupFileName, this.initialThingToDoFileName);
		if(destinationPath != null) {
			LocalDateTime originFileDateModified = this.utilFileService.getDateModified(this.initialThingToDoFileName);
			this.fileBackupRepository.saveFileBackup(destinationPath, originFileDateModified, Flag.THING_TO_DO);
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

	public String getBackupFileName() {
		return backupFileName;
	}

	public void setBackupFileName(String backupFileName) {
		this.backupFileName = backupFileName;
	}

	public String getInitialThingToDoFileName() {
		return initialThingToDoFileName;
	}

	public void setInitialThingToDoFileName(String intialThingToDoFileName) {
		this.initialThingToDoFileName = intialThingToDoFileName;
	}

	public String getFinalThingToDoFileName() {
		return finalThingToDoFileName;
	}

	public void setFinalThingToDoFileName(String finalThingToDoFileName) {
		this.finalThingToDoFileName = finalThingToDoFileName;
	}


}
