package com.faap.scheduler.job_application.excel.models.done;

import java.util.List;

public class FindUnitResponse {
	private int statusCode;
	private String message;
	private List<Unit> payload;
	
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
	public List<Unit> getPayload() {
		return payload;
	}
	public void setPayload(List<Unit> payload) {
		this.payload = payload;
	}
	
	
}
