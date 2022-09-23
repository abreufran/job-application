package com.faap.scheduler.job_application;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
        
        ((TradeTask)tradeTask).readFileAndSave();
        
        int seconds = 300;
        
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadJobFile(Task task) {
    	Timer timer = new Timer();
        TimerTask jobTask = new JobTask(task);
        
        try {
			((JobTask)jobTask).readExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        int seconds = 300;
        
        timer.schedule(jobTask, 0, seconds * 1000);
    }
}
