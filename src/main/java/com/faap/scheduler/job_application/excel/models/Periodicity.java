package com.faap.scheduler.job_application.excel.models;

import java.util.Arrays;

public enum Periodicity {
	EVERY_WEEK("Every week", 7),
	EVERY_2_WEEKS("Every 2 weeks", 14),
	EVERY_3_WEEKS("Every 3 weeks", 21),
	LAST_DAY_MONTH("Last day of the month", -1),
	EVERY_YEAR("Every year", -1);
	
	private String name;
	private int size;
	
	private Periodicity(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}
	
	public static Periodicity getPeriodicity(String periodicity) {
		return Arrays.asList(Periodicity.values()).stream().filter(p -> p.getName().equals(periodicity)).findFirst().orElse(null);
	}
}
