package com.faap.scheduler.job_application.models.thing_to_do;


public class SaveRequestDto {
	private String jsonParams;
	private String flag;
	private String saveType;
	
	public String getJsonParams() {
		return jsonParams;
	}
	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}
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
	
	
}
