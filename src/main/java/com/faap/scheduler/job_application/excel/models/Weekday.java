package com.faap.scheduler.job_application.excel.models;

public enum Weekday {
	ANY("Any", -1),
	MONDAY("Monday", 1),
	TUESDAY("Tuesday", 2),
	FRIDAY("Friday", 3),
	SATURDAY("Saturday", 6);
	
	private String name;
	private int value;
	
	private Weekday(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}
