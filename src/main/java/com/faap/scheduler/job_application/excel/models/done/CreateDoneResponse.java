package com.faap.scheduler.job_application.excel.models.done;

public class CreateDoneResponse {
	private int statusCode;
	private String message;
	private Done payload;
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
	public Done getPayload() {
		return payload;
	}
	public void setPayload(Done payload) {
		this.payload = payload;
	}
	@Override
	public String toString() {
		return "CreateDailyTaskResponse [statusCode=" + statusCode + ", message=" + message + ", payload=" + payload
				+ "]";
	}
	
	
}
