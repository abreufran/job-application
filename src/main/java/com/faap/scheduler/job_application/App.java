package com.faap.scheduler.job_application;

import java.util.Timer;
import java.util.TimerTask;

import com.faap.scheduler.job_application.tasks.TradeTask;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Timer timer = new Timer();
        TimerTask tradeTask = new TradeTask();
        
        ((TradeTask)tradeTask).readFileAndSave();
        
        int seconds = 300;
        
        timer.schedule(tradeTask, 0, seconds * 1000);
    }
}
