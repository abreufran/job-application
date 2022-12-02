package com.faap.scheduler.job_application.models.thing_to_do;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ThingToDo {
	private int id;
	
	private String jsonParams;
	
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private LocalDate incidenceDate;
	
	private LocalDate executionDate;
	
	private LocalDate estimatedDate;
	
	private String priority;
	
	private String thingToDo;
	
	private String category;
	
	private String status;
	
	private String token;
	
	private boolean emailSent;
	
	private boolean estimatedDateSent;

	private String userName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getIncidenceDate() {
		return incidenceDate;
	}

	public void setIncidenceDate(LocalDate incidenceDate) {
		this.incidenceDate = incidenceDate;
	}

	public LocalDate getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(LocalDate executionDate) {
		this.executionDate = executionDate;
	}

	public LocalDate getEstimatedDate() {
		return estimatedDate;
	}

	public void setEstimatedDate(LocalDate estimatedDate) {
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

	@Override
	public String toString() {
		return "ThingToDo [id=" + id + ", jsonParams=" + jsonParams + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", incidenceDate=" + incidenceDate + ", executionDate=" + executionDate
				+ ", estimatedDate=" + estimatedDate + ", priority=" + priority + ", thingToDo=" + thingToDo
				+ ", category=" + category + ", status=" + status + ", token=" + token + ", emailSent=" + emailSent
				+ ", estimatedDateSent=" + estimatedDateSent + ", userName=" + userName + "]";
	}
	
	
	
}
