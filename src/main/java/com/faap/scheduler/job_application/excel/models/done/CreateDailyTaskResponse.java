package com.faap.scheduler.job_application.excel.models.done;

public class CreateDailyTaskResponse {
	private int statusCode;
	private String message;
	private DailyTask payload;
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
	public DailyTask getPayload() {
		return payload;
	}
	public void setPayload(DailyTask payload) {
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "CreateDailyTaskResponse [statusCode=" + statusCode + ", message=" + message + ", payload=" + payload
				+ "]";
	}
	
	
}
