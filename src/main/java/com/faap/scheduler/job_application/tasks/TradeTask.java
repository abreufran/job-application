package com.faap.scheduler.job_application.tasks;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class TradeTask extends TimerTask {

	@Override
	public void run() {
		System.out.println(LocalDateTime.now());
	}

}
