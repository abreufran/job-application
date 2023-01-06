package com.faap.scheduler.job_application.excel.models.done;

public class DoneTemplateDto {
	private Integer id;
	
	private Integer defaultInitialHour;
	
	private Integer defaultInitialMinute;
	
	private Integer defaultFinalHour;

	private Integer defaultFinalMinute;
	
	private String description;
	
	private String defaultDiscipline;

	private Double defaultResult;
	
	private String defaultUnit;
	
	private String observation;
	
	private String channel;

	private Integer customerId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDefaultInitialHour() {
		return defaultInitialHour;
	}

	public void setDefaultInitialHour(Integer defaultInitialHour) {
		this.defaultInitialHour = defaultInitialHour;
	}

	public Integer getDefaultInitialMinute() {
		return defaultInitialMinute;
	}

	public void setDefaultInitialMinute(Integer defaultInitialMinute) {
		this.defaultInitialMinute = defaultInitialMinute;
	}

	public Integer getDefaultFinalHour() {
		return defaultFinalHour;
	}

	public void setDefaultFinalHour(Integer defaultFinalHour) {
		this.defaultFinalHour = defaultFinalHour;
	}

	public Integer getDefaultFinalMinute() {
		return defaultFinalMinute;
	}

	public void setDefaultFinalMinute(Integer defaultFinalMinute) {
		this.defaultFinalMinute = defaultFinalMinute;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultDiscipline() {
		return defaultDiscipline;
	}

	public void setDefaultDiscipline(String defaultDiscipline) {
		this.defaultDiscipline = defaultDiscipline;
	}

	public Double getDefaultResult() {
		return defaultResult;
	}

	public void setDefaultResult(Double defaultResult) {
		this.defaultResult = defaultResult;
	}

	public String getDefaultUnit() {
		return defaultUnit;
	}

	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
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
		return "DoneTemplateDto [id=" + id + ", defaultInitialHour=" + defaultInitialHour + ", defaultInitialMinute="
				+ defaultInitialMinute + ", defaultFinalHour=" + defaultFinalHour + ", defaultFinalMinute="
				+ defaultFinalMinute + ", description=" + description + ", defaultDiscipline=" + defaultDiscipline
				+ ", defaultResult=" + defaultResult + ", defaultUnit=" + defaultUnit + ", observation=" + observation
				+ ", channel=" + channel + ", customerId=" + customerId + "]";
	}
	
	
}
