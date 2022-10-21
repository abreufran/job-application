package com.faap.scheduler.job_application.excel.models;

import java.util.Arrays;

public enum Weekday {
	ANY("Any", -1),
	MONDAY("Monday", 1),
	TUESDAY("Tuesday", 2),
	WEDNESDAY("Wednesday", 3),
	THURSDAY("Thursday", 4),
	FRIDAY("Friday", 5),
	SATURDAY("Saturday", 6),
	SUNDAY("Sunday", 7);
	
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
	
	public static Weekday getWeekday(String weekday) {
		Weekday weekdayEnum = Arrays.asList(Weekday.values()).stream().filter(p -> p.getName().equals(weekday)).findFirst().orElse(null);
		if(weekdayEnum == null) System.out.println("Weekday Enum does not exist: " + weekdayEnum);
		return weekdayEnum;
	}
}
