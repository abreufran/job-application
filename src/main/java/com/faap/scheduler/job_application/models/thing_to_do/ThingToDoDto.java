package com.faap.scheduler.job_application.models.thing_to_do;

import java.util.List;

import com.faap.scheduler.job_application.excel.models.CellTypeWrapper;
import com.faap.scheduler.job_application.excel.models.RowWrapper;
import com.faap.scheduler.job_application.excel.models.ThingToDoColumnType;
import com.faap.scheduler.job_application.excel.dtos.Constants;

public class ThingToDoDto {
	private int id;
	
	private String incidenceDate;
	
	private String executionDate;
	
	private String estimatedDate;
	
	private String priority;
	
	private String thingToDo;
	
	private String category;
	
	private String status;
	
	private String token;
	
	private boolean emailSent;
	
	private boolean estimatedDateSent;
	
	private String userName;
	
	public ThingToDoDto() {}
	
	public ThingToDoDto(RowWrapper rowWrapper) {
		
		rowWrapper.getCellWrapperList().forEach(cw -> {
			if(ThingToDoColumnType.ID.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setToken(cw.getCellValue());
			}
			if(ThingToDoColumnType.INCIDENCE_DATE.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setIncidenceDate(cw.getCellValue());
			}
			if(ThingToDoColumnType.EXECUTION_DATE.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setExecutionDate(cw.getCellValue());
			}
			if(ThingToDoColumnType.ESTIMATED_DATE.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setEstimatedDate(cw.getCellValue());
			}
			if(ThingToDoColumnType.PRIORITY.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setPriority(cw.getCellValue());
			}
			if(ThingToDoColumnType.THINGS_TO_DO.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setThingToDo(cw.getCellValue());
			}
			if(ThingToDoColumnType.CATEGORY.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setCategory(cw.getCellValue());
			}
			if(ThingToDoColumnType.STATUS.getColumnIndex() == cw.getCellTypeWrapper().getColumnIndex()) {
				this.setStatus(cw.getCellValue());
			}
			this.setUserName(Constants.USER_NAME);
		});
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIncidenceDate() {
		return incidenceDate;
	}

	public void setIncidenceDate(String incidenceDate) {
		this.incidenceDate = incidenceDate;
	}

	public String getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}

	public String getEstimatedDate() {
		return estimatedDate;
	}

	public void setEstimatedDate(String estimatedDate) {
		this.estimatedDate = estimatedDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getThingToDo() {
		return thingToDo;
	}

	public void setThingToDo(String thingToDo) {
		this.thingToDo = thingToDo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isEmailSent() {
		return emailSent;
	}

	public void setEmailSent(boolean emailSent) {
		this.emailSent = emailSent;
	}

	public boolean isEstimatedDateSent() {
		return estimatedDateSent;
	}

	public void setEstimatedDateSent(boolean estimatedDateSent) {
		this.estimatedDateSent = estimatedDateSent;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
