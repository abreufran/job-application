package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;

public class SaveAllResponseDto {
	private int statusCode;
	private String message;
	private List<DataListResponse> responseList;
	
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
	public List<DataListResponse> getResponseList() {
		return responseList;
	}
	public void setResponseList(List<DataListResponse> responseList) {
		this.responseList = responseList;
	}
	
	
	
}
