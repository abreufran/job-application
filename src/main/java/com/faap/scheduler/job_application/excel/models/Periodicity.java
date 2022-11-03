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
	EVERY_9_WEEKS("Every 9 weeks", 63),
	EVERY_10_WEEKS("Every 10 weeks", 70),
	
	EVERY_1_MONTH("Every 1 month", 30),
	EVERY_2_MONTHS("Every 2 months", 60),
	EVERY_3_MONTHS("Every 3 months", 90),
	EVERY_4_MONTHS("Every 4 months", 120),
	EVERY_5_MONTHS("Every 5 months", 150),
	EVERY_6_MONTHS("Every 6 months", 180),
	EVERY_7_MONTHS("Every 7 months", 210),
	EVERY_8_MONTHS("Every 8 months", 240),
	EVERY_9_MONTHS("Every 9 months", 270),
	EVERY_10_MONTHS("Every 10 months", 300),
	
	LAST_DAY_MONTH("Last day of the month", -1),
	FIRST_DAY_DECEMBER("First day of december", -1),
	EVERY_SEVENTEENTH_DAY("Every seventeenth day", -1),
	EVERY_FIFTH_DAY("Every fifth day", -1),
	
	EVERY_1_DAY("Every 1 day", 1),
	EVERY_2_DAYS("Every 2 days", 2),
	EVERY_3_DAYS("Every 3 days", 3),
	EVERY_4_DAYS("Every 4 days", 4),
	EVERY_5_DAYS("Every 5 days", 5),
	EVERY_6_DAYS("Every 6 days", 6),
	EVERY_7_DAYS("Every 7 days", 7),
	EVERY_8_DAYS("Every 8 days", 8),
	EVERY_9_DAYS("Every 9 days", 9),
	EVERY_10_DAYS("Every 10 days", 10),
	EVERY_11_DAYS("Every 11 days", 11),
	EVERY_12_DAYS("Every 12 days", 12),
	EVERY_13_DAYS("Every 13 days", 13),
	EVERY_14_DAYS("Every 14 days", 14),
	EVERY_15_DAYS("Every 15 days", 15),
	EVERY_16_DAYS("Every 16 days", 16),
	EVERY_17_DAYS("Every 17 days", 17),
	EVERY_18_DAYS("Every 18 days", 18),
	EVERY_19_DAYS("Every 19 days", 19),
	EVERY_20_DAYS("Every 20 days", 20),
	EVERY_21_DAYS("Every 21 days", 21),
	EVERY_22_DAYS("Every 22 days", 22),
	EVERY_23_DAYS("Every 23 days", 23),
	EVERY_24_DAYS("Every 24 days", 24),
	EVERY_25_DAYS("Every 25 days", 25),
	EVERY_26_DAYS("Every 26 days", 26),
	EVERY_27_DAYS("Every 27 days", 27),
	EVERY_28_DAYS("Every 28 days", 28),
	EVERY_29_DAYS("Every 29 days", 29),
	EVERY_30_DAYS("Every 30 days", 30);
	
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
