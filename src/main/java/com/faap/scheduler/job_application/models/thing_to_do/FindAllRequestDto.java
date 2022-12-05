package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;

public class FindAllRequestDto {
	private List<JsonParam> jsonParamList;
	private String flag;
	private String findType;
	
	
	
	public List<JsonParam> getJsonParamList() {
		return jsonParamList;
	}
	public void setJsonParamList(List<JsonParam> jsonParamList) {
		this.jsonParamList = jsonParamList;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	
	
}
