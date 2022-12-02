package com.faap.scheduler.job_application.models.thing_to_do;


public class SaveResponseDto {
	private int statusCode;
	private String message;
	private ThingToDo data;
	
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
	public ThingToDo getData() {
		return data;
	}
	public void setData(ThingToDo data) {
		this.data = data;
	}
	
	
}
