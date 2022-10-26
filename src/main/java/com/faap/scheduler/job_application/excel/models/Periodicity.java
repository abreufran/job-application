package com.faap.scheduler.job_application.excel.models;

import java.util.Arrays;

public enum Periodicity {
	EVERY_WEEK("Every week", 7),
	EVERY_2_WEEKS("Every 2 weeks", 14),
	EVERY_3_WEEKS("Every 3 weeks", 21),
	EVERY_4_WEEKS("Every 4 weeks", 28),
	EVERY_5_WEEKS("Every 5 weeks", 35),
	EVERY_6_WEEKS("Every 6 weeks", 42),
	EVERY_7_WEEKS("Every 7 weeks", 49),
	EVERY_8_WEEKS("Every 8 weeks", 56),
	LAST_DAY_MONTH("Last day of the month", -1),
	FIRST_DAY_DECEMBER("First day of december", -1),
	EVERY_SEVENTEENTH_DAY("Every seventeenth day", -1),
	EVERY_FIFTH_DAY("Every fifth day", -1);
	
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
		Periodicity periodicityEnum = Arrays.asList(Periodicity.values()).stream().filter(p -> p.getName().equals(periodicity)).findFirst().orElse(null);
		if(periodicityEnum == null) System.out.println("Periodicity Enum does not exist: " + periodicity);
		return periodicityEnum;
	}
}
