package com.faap.scheduler.job_application;

import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.ThingToDoExcelService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.file.services.SecretaryService;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;
import com.faap.scheduler.job_application.tasks.ThingToDoTask;
import com.faap.scheduler.job_application.tasks.TradeTask;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static String BACKUP_PATH = "C://Users/Administrator/Desktop/thing_to_do_backup";
	public static String THING_TO_DO_FILE_NAME = "G://My Drive/Things_to_do.xlsx";
	
    public static void main( String[] args )
    {
    	SecretaryService secretaryService = new SecretaryService();
    	FileBackupRepository fileBackupRepository = new FileBackupRepository();
    	DataFileRepository dataFileRepository = new DataFileRepository();
    	UtilDateService utilDateService = new UtilDateService();
    	UtilExcelService utilExcelService = new UtilExcelService(utilDateService);
    	ExcelReadService excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	ExcelWriteService excelWriteService = new ExcelWriteService(utilDateService, utilExcelService);
    	ThingToDoExcelService thingToDoExcelService = new ThingToDoExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService, secretaryService);
    	UtilFileService utilFileService = new UtilFileService();
    	
    	
        App.runReadTraderFile(dataFileRepository);
        App.runReadThingToDoFile(dataFileRepository, thingToDoExcelService, utilExcelService, utilFileService, fileBackupRepository, secretaryService);
    }
    
    public static void runReadTraderFile(DataFileRepository task) {   	
        TimerTask tradeTask = new TradeTask(task);
        
        int seconds = 300;
        
        Timer timer = new Timer();
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadThingToDoFile(DataFileRepository dataFileRepository, ThingToDoExcelService thingToDoExcelService, 
    		UtilExcelService utilExcelService,
    		UtilFileService utilFileService, 
    		FileBackupRepository fileBackupRepository,
    		SecretaryService secretaryService) {   	
        TimerTask thingToDoTask = new ThingToDoTask(dataFileRepository, thingToDoExcelService, utilExcelService, utilFileService, fileBackupRepository, secretaryService,
        		BACKUP_PATH, THING_TO_DO_FILE_NAME, THING_TO_DO_FILE_NAME);
        
        int seconds = 30;
        
        Timer timer = new Timer();
        timer.schedule(thingToDoTask, 0, seconds * 1000);
    }
}
