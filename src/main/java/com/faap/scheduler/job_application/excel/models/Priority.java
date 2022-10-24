package com.faap.scheduler.job_application.excel.models;

public enum Priority {
	A1(7),
	A2(30),
	B1(60),
	C1(366);
	
	private int dayNumberToEstimatedDate;
	
	private Priority(int dayNumberToEstimatedDate) {
		this.dayNumberToEstimatedDate = dayNumberToEstimatedDate;
	}

	public int getDayNumberToEstimatedDate() {
		return dayNumberToEstimatedDate;
	}
}
