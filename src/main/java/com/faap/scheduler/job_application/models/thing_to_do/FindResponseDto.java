package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;

public class FindResponseDto {
	private int statusCode;
	private String message;
	private List<ThingToDo> dataList;
	
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
	public List<ThingToDo> getDataList() {
		return dataList;
	}
	public void setDataList(List<ThingToDo> dataList) {
		this.dataList = dataList;
	}
	@Override
	public String toString() {
		return "FindResponseDto [statusCode=" + statusCode + ", message=" + message + ", dataList=" + dataList + "]";
	}
	
	
	
}
