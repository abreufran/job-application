package com.faap.scheduler.job_application.file.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UtilDateService {
	
	public static String READ_EXCEL_DATE_FORMAT = "ddMMyyyy";
	
	public static String WRITE_EXCEL_DATE_FORMAT = "dd/mm/yyyy";

	public String getStrDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(READ_EXCEL_DATE_FORMAT);
		return dateFormat.format(date);
	}
	
	public LocalDate getLocalDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(READ_EXCEL_DATE_FORMAT);
		//convert String to LocalDate
		return LocalDate.parse(date, formatter);
	}
}
