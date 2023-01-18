package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;

public class FindDailyCategoryResponse {
	private int statusCode;
	private String message;
	private List<DailyCategory> payload;
	
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
	public List<DailyCategory> getPayload() {
		return payload;
	}
	public void setPayload(List<DailyCategory> payload) {
		this.payload = payload;
	}
	
	
}
