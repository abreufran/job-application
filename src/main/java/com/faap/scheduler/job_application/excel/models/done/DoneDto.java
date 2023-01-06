package com.faap.scheduler.job_application.excel.models.done;

public class DoneDto {
	private Integer id;
	
	private String date;
	
	private Integer initialHour;
	
	private Integer initialMinute;
	
	private Integer finalHour;

	private Integer finalMinute;
	
	private String description;
	
	private String discipline;

	private Double result;
	
	private String unit;
	
	private String observation;
	
	private String channel;

	private Integer customerId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getInitialHour() {
		return initialHour;
	}

	public void setInitialHour(Integer initialHour) {
		this.initialHour = initialHour;
	}

	public Integer getInitialMinute() {
		return initialMinute;
	}

	public void setInitialMinute(Integer initialMinute) {
		this.initialMinute = initialMinute;
	}

	public Integer getFinalHour() {
		return finalHour;
	}

	public void setFinalHour(Integer finalHour) {
		this.finalHour = finalHour;
	}

	public Integer getFinalMinute() {
		return finalMinute;
	}

	public void setFinalMinute(Integer finalMinute) {
		this.finalMinute = finalMinute;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	@Override
	public String toString() {
		return "DoneDto [id=" + id + ", date=" + date + ", initialHour=" + initialHour + ", initialMinute="
				+ initialMinute + ", finalHour=" + finalHour + ", finalMinute=" + finalMinute + ", description="
				+ description + ", discipline=" + discipline + ", result=" + result + ", unit=" + unit
				+ ", observation=" + observation + ", channel=" + channel + ", customerId=" + customerId + "]";
	}
	
	
}
