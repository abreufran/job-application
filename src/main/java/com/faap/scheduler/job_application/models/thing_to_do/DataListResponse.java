package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;


public class DataListResponse {
	private String token;
	private List<ThingToDo> dataList;
	private SaveType saveType;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<ThingToDo> getDataList() {
		return dataList;
	}
	public void setDataList(List<ThingToDo> dataList) {
		this.dataList = dataList;
	}
	public SaveType getSaveType() {
		return saveType;
	}
	public void setSaveType(SaveType saveType) {
		this.saveType = saveType;
	}
	
	
}
