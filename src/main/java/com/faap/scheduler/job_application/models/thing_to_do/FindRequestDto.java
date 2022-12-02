package com.faap.scheduler.job_application.models.thing_to_do;



public class FindRequestDto {
	private String jsonParams;
	private String flag;
	private String findType;
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
	public String getFindType() {
		return findType;
	}
	public void setFindType(String findType) {
		this.findType = findType;
	}
	
	
}
