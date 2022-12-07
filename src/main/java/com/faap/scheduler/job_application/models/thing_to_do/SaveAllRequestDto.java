package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;

public class SaveAllRequestDto {
	private List<JsonParam> jsonParamList;
	private String flag;
	private String saveType;
	
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSaveType() {
		return saveType;
	}
	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	public List<JsonParam> getJsonParamList() {
		return jsonParamList;
	}
	public void setJsonParamList(List<JsonParam> jsonParamList) {
		this.jsonParamList = jsonParamList;
	}
	
	
}
