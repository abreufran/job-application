package com.faap.scheduler.job_application;

import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.excel.services.ExcelReadService;
import com.faap.scheduler.job_application.excel.services.ExcelWriteService;
import com.faap.scheduler.job_application.excel.services.UtilExcelService;
import com.faap.scheduler.job_application.excel.services.job.JobExcelService;
import com.faap.scheduler.job_application.file.services.UtilDateService;
import com.faap.scheduler.job_application.file.services.UtilFileService;
import com.faap.scheduler.job_application.repositories.DataFileRepository;
import com.faap.scheduler.job_application.repositories.FileBackupRepository;
import com.faap.scheduler.job_application.tasks.JobTask;
import com.faap.scheduler.job_application.tasks.TradeTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	FileBackupRepository fileBackupRepository = new FileBackupRepository();
    	DataFileRepository dataFileRepository = new DataFileRepository();
    	UtilDateService utilDateService = new UtilDateService();
    	UtilExcelService utilExcelService = new UtilExcelService(utilDateService);
    	ExcelReadService excelReadService = new ExcelReadService(utilDateService, utilExcelService);
    	ExcelWriteService excelWriteService = new ExcelWriteService(utilDateService);
    	JobExcelService jobExcelService = new JobExcelService(utilDateService, utilExcelService, excelReadService, excelWriteService);
    	UtilFileService utilFileService = new UtilFileService();
    	
        App.runReadTraderFile(dataFileRepository);
        App.runReadJobFile(dataFileRepository, jobExcelService, utilExcelService, utilFileService, fileBackupRepository);
    }
    
    public static void runReadTraderFile(DataFileRepository task) {   	
        TimerTask tradeTask = new TradeTask(task);
        
        int seconds = 300;
        
        Timer timer = new Timer();
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadJobFile(DataFileRepository taskRepository, JobExcelService jobExcelService, 
    		UtilExcelService utilExcelService,
    		UtilFileService utilFileService, FileBackupRepository fileBackupRepository) {   	
        TimerTask jobTask = new JobTask(taskRepository, jobExcelService, utilExcelService, utilFileService, fileBackupRepository);
        
        int seconds = 300;
        
        Timer timer = new Timer();
        timer.schedule(jobTask, 0, seconds * 1000);
    }
}
