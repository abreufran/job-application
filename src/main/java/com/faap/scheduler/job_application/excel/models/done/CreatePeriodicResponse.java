package com.faap.scheduler.job_application.excel.models.done;

public class CreatePeriodicResponse {
	private int statusCode;
	private String message;
	private Periodic payload;
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
	public Periodic getPayload() {
		return payload;
	}
	public void setPayload(Periodic payload) {
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "CreateDailyTaskResponse [statusCode=" + statusCode + ", message=" + message + ", payload=" + payload
				+ "]";
	}
	
	
}
