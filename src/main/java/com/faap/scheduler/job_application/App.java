package com.faap.scheduler.job_application;

import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.excel.services.JobExcelService;
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
    	JobExcelService jobExcelService = new JobExcelService(utilDateService);
    	UtilFileService utilFileService = new UtilFileService();
    	
        App.runReadTraderFile(dataFileRepository);
        App.runReadJobFile(dataFileRepository, jobExcelService, utilFileService, fileBackupRepository);
    }
    
    public static void runReadTraderFile(DataFileRepository task) {   	
        TimerTask tradeTask = new TradeTask(task);
        
        int seconds = 300;
        
        Timer timer = new Timer();
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadJobFile(DataFileRepository taskRepository, JobExcelService jobExcelService, 
    		UtilFileService utilFileService, FileBackupRepository fileBackupRepository) {   	
        TimerTask jobTask = new JobTask(taskRepository, jobExcelService, utilFileService, fileBackupRepository);
        
        int seconds = 300;
        
        Timer timer = new Timer();
        timer.schedule(jobTask, 0, seconds * 1000);
    }
}
