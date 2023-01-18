package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;


public class FindWeekdayResponse {
	private int statusCode;
	private String message;
	private List<Weekday> payload;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Weekday> getPayload() {
		return payload;
	}
	public void setPayload(List<Weekday> payload) {
		this.payload = payload;
	}
	
	
}
