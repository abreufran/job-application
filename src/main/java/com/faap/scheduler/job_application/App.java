package com.faap.scheduler.job_application;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.tasks.Job;
import com.faap.scheduler.job_application.tasks.TradeTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        App.runReadTraderFile();
        App.runReadJobFile();
    }
    
    public static void runReadTraderFile() {
    	Timer timer = new Timer();
        TimerTask tradeTask = new TradeTask();
        
        ((TradeTask)tradeTask).readFileAndSave();
        
        int seconds = 300;
        
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
    
    public static void runReadJobFile() {
    	Timer timer = new Timer();
        TimerTask jobTask = new Job();
        
        try {
			((Job)jobTask).readExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        int seconds = 300;
        
        timer.schedule(jobTask, 0, seconds * 1000);
    }
}
