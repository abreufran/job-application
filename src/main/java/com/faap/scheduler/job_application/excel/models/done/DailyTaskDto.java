package com.faap.scheduler.job_application.excel.models.done;

public class DailyTaskDto {
	private Integer id;
	
	private String incidenceDate;
	
	private String executionDate;
	
	private String estimatedDate;
	
	private String priority;

	private String description;
	
	private String dailyCategory;
	
	private String status;

	private String observation;
	
	private String channel;

	private Integer customerId;
	
	private Integer dayNumberOfIncidenceDate;
	
	private Integer dayNumberOfExecutionDate;
	
	private Integer dayNumberOfEstimatedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDailyCategory() {
		return dailyCategory;
	}

	public void setDailyCategory(String dailyCategory) {
		this.dailyCategory = dailyCategory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getDayNumberOfIncidenceDate() {
		return dayNumberOfIncidenceDate;
	}

	public void setDayNumberOfIncidenceDate(Integer dayNumberOfIncidenceDate) {
		this.dayNumberOfIncidenceDate = dayNumberOfIncidenceDate;
	}

	public Integer getDayNumberOfExecutionDate() {
		return dayNumberOfExecutionDate;
	}

	public void setDayNumberOfExecutionDate(Integer dayNumberOfExecutionDate) {
		this.dayNumberOfExecutionDate = dayNumberOfExecutionDate;
	}

	public Integer getDayNumberOfEstimatedDate() {
		return dayNumberOfEstimatedDate;
	}

	public void setDayNumberOfEstimatedDate(Integer dayNumberOfEstimatedDate) {
		this.dayNumberOfEstimatedDate = dayNumberOfEstimatedDate;
	}
	
	
}
