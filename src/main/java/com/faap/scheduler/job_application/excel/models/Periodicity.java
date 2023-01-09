package com.faap.scheduler.job_application.excel.models;

import java.util.Arrays;

public enum Periodicity {
	EVERY_WEEK("Every week", 7, -1),
	EVERY_2_WEEKS("Every 2 weeks", 14, -1),
	EVERY_3_WEEKS("Every 3 weeks", 21, -1),
	EVERY_4_WEEKS("Every 4 weeks", 28, -1),
	EVERY_5_WEEKS("Every 5 weeks", 35, -1),
	EVERY_6_WEEKS("Every 6 weeks", 42, -1),
	EVERY_7_WEEKS("Every 7 weeks", 49, -1),
	EVERY_8_WEEKS("Every 8 weeks", 56, -1),
	EVERY_9_WEEKS("Every 9 weeks", 63, -1),
	EVERY_10_WEEKS("Every 10 weeks", 70, -1),
	EVERY_20_WEEKS("Every 20 weeks", 140, -1),
	
	EVERY_1_MONTH("Every 1 month", 30, -1),
	EVERY_2_MONTHS("Every 2 months", 60, -1),
	EVERY_3_MONTHS("Every 3 months", 90, -1),
	EVERY_4_MONTHS("Every 4 months", 120, -1),
	EVERY_5_MONTHS("Every 5 months", 150, -1),
	EVERY_6_MONTHS("Every 6 months", 180, -1),
	EVERY_7_MONTHS("Every 7 months", 210, -1),
	EVERY_8_MONTHS("Every 8 months", 240, -1),
	EVERY_9_MONTHS("Every 9 months", 270, -1),
	EVERY_10_MONTHS("Every 10 months", 300, -1),
	
	FIRST_DAY_DECEMBER("First day of december", -1, -1),
	
	SECOND_SATURDAY_NOVEMBER("Second saturday of november", -1, -1),
	
	LAST_DAY_MONTH("Last day of the month", -1, -1),
	
	EVERY_FIRST_DAY("Every first day", -1, 1),
	EVERY_SECOND_DAY("Every second day", -1, 2),
	EVERY_THIRD_DAY("Every third day", -1, 3),
	EVERY_FOURTH_DAY("Every fourth day", -1, 4),
	EVERY_FIFTH_DAY("Every fifth day", -1, 5),
	EVERY_SIXTH_DAY("Every sixth day", -1, 6),
	EVERY_SEVENTH_DAY("Every seventh day", -1, 7),
	EVERY_EIGHTH_DAY("Every eighth day", -1, 8),
	EVERY_NINTH_DAY("Every ninth day", -1, 9),
	EVERY_TENTH_DAY("Every tenth day", -1, 10),
	EVERY_ELEVENTH_DAY("Every eleventh day", -1, 11),
	EVERY_TWELFTH_DAY("Every twelfth day", -1, 12),
	EVERY_THIRTEENTH_DAY("Every thirteenth day", -1, 13),
	EVERY_FOURTEENTH_DAY("Every fourteenth day", -1, 14),
	EVERY_FIFTEENTH_DAY("Every fifteenth day", -1, 15),
	EVERY_SIXTEENTH_DAY("Every sixteenth day", -1, 16),
	EVERY_SEVENTEENTH_DAY("Every seventeenth day", -1, 17),
	EVERY_EIGHTEENTH_DAY("Every eighteenth day", -1, 18),
	EVERY_NINETEENTH_DAY("Every nineteenth day", -1, 19),
	EVERY_TWENTIETH_DAY("Every twentieth day", -1, 20),
	EVERY_TWENTY_FIRST_DAY("Every twenty first day", -1, 21),
	EVERY_TWENTY_SECOND_DAY("Every twenty second day", -1, 22),
	EVERY_TWENTY_THIRD_DAY("Every twenty third day", -1, 23),
	EVERY_TWENTY_FOURTH_DAY("Every twenty fourth day", -1, 24),
	EVERY_TWENTY_FIFTH_DAY("Every twenty fifth day", -1, 25),
	EVERY_TWENTY_SIXTH_DAY("Every twenty sixth day", -1, 26),
	EVERY_TWENTY_SEVENTH_DAY("Every twenty seventh day", -1, 27),
	EVERY_TWENTY_EIGHTH_DAY("Every twenty eighth day", -1, 28),
	EVERY_TWENTY_NINTH_DAY("Every twenty ninth day", -1, 29),
	EVERY_THIRTY_DAY("Every thirty day", -1, 30),
	EVERY_THIRTY_ONE_DAY("Every thirty first day", -1, 31),
	
	EVERY_1_DAY("Every 1 day", 1, -1),
	EVERY_2_DAYS("Every 2 days", 2, -1),
	EVERY_3_DAYS("Every 3 days", 3, -1),
	EVERY_4_DAYS("Every 4 days", 4, -1),
	EVERY_5_DAYS("Every 5 days", 5, -1),
	EVERY_6_DAYS("Every 6 days", 6, -1),
	EVERY_7_DAYS("Every 7 days", 7, -1),
	EVERY_8_DAYS("Every 8 days", 8, -1),
	EVERY_9_DAYS("Every 9 days", 9, -1),
	EVERY_10_DAYS("Every 10 days", 10, -1),
	EVERY_11_DAYS("Every 11 days", 11, -1),
	EVERY_12_DAYS("Every 12 days", 12, -1),
	EVERY_13_DAYS("Every 13 days", 13, -1),
	EVERY_14_DAYS("Every 14 days", 14, -1),
	EVERY_15_DAYS("Every 15 days", 15, -1),
	EVERY_16_DAYS("Every 16 days", 16, -1),
	EVERY_17_DAYS("Every 17 days", 17, -1),
	EVERY_18_DAYS("Every 18 days", 18, -1),
	EVERY_19_DAYS("Every 19 days", 19, -1),
	EVERY_20_DAYS("Every 20 days", 20, -1),
	EVERY_21_DAYS("Every 21 days", 21, -1),
	EVERY_22_DAYS("Every 22 days", 22, -1),
	EVERY_23_DAYS("Every 23 days", 23, -1),
	EVERY_24_DAYS("Every 24 days", 24, -1),
	EVERY_25_DAYS("Every 25 days", 25, -1),
	EVERY_26_DAYS("Every 26 days", 26, -1),
	EVERY_27_DAYS("Every 27 days", 27, -1),
	EVERY_28_DAYS("Every 28 days", 28, -1),
	EVERY_29_DAYS("Every 29 days", 29, -1),
	EVERY_30_DAYS("Every 30 days", 30, -1);
	
	private String name;
	private int size;
	private int monthDayNumber;
	
	private Periodicity(String name, int size, int monthDayNumber) {
		this.name = name;
		this.size = size;
		this.monthDayNumber = monthDayNumber;
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

	public int getMonthDayNumber() {
		return monthDayNumber;
	}
}
