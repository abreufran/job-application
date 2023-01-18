package com.faap.scheduler.job_application.excel.models.done;

public class PeriodicDto {
	private Integer id;
	
	private String initialDate;
	
	private String finalDate;
	
	private String description;
	
	private String priority;

	private String dailyCategory;
	
	private String periodicity;
	
	private String weekday;
	
	private String status;

	private String observation;
	
	private String channel;

	private Integer customerId;
	
	private Integer dayNumberOfInitialDate;
	
	private Integer dayNumberOfFinalDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(String initialDate) {
		this.initialDate = initialDate;
	}

	public String getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(String finalDate) {
		this.finalDate = finalDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDailyCategory() {
		return dailyCategory;
	}

	public void setDailyCategory(String dailyCategory) {
		this.dailyCategory = dailyCategory;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
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

	public Integer getDayNumberOfInitialDate() {
		return dayNumberOfInitialDate;
	}

	public void setDayNumberOfInitialDate(Integer dayNumberOfInitialDate) {
		this.dayNumberOfInitialDate = dayNumberOfInitialDate;
	}

	public Integer getDayNumberOfFinalDate() {
		return dayNumberOfFinalDate;
	}

	public void setDayNumberOfFinalDate(Integer dayNumberOfFinalDate) {
		this.dayNumberOfFinalDate = dayNumberOfFinalDate;
	}
	
	
}
