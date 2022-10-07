package com.faap.scheduler.job_application;

import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.excel.services.JobExcelService;
import com.faap.scheduler.job_application.tasks.JobTask;
import com.faap.scheduler.job_application.tasks.Task;
import com.faap.scheduler.job_application.tasks.TradeTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Task task = new Task();
        App.runReadTraderFile(task);
        App.runReadJobFile(task);
    }
    
    public static void runReadTraderFile(Task task) {
    	Timer timer = new Timer();
        TimerTask tradeTask = new TradeTask(task);
        
        int seconds = 300;
        
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadJobFile(Task task) {
    	Timer timer = new Timer();
    	JobExcelService jobExcelService = new JobExcelService();
        TimerTask jobTask = new JobTask(task, jobExcelService);
        
        int seconds = 300;
        
        timer.schedule(jobTask, 0, seconds * 1000);
    }
}
