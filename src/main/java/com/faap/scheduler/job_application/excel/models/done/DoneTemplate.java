package com.faap.scheduler.job_application.excel.models.done;

import java.time.LocalDateTime;


public class DoneTemplate {

	private Integer id;
	
	private Integer defaultInitialHour;

	private Integer defaultInitialMinute;
	
	private Integer defaultFinalHour;

	private Integer defaultFinalMinute;
	
	private String description;
	
	private Discipline defaultDiscipline;
	
	private Double defaultResult;
	
	private Unit defaultUnit;
	
	private Channel channel;
	
	private Customer customer;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

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

	public Discipline getDefaultDiscipline() {
		return defaultDiscipline;
	}

	public void setDefaultDiscipline(Discipline defaultDiscipline) {
		this.defaultDiscipline = defaultDiscipline;
	}

	public Double getDefaultResult() {
		return defaultResult;
	}

	public void setDefaultResult(Double defaultResult) {
		this.defaultResult = defaultResult;
	}

	public Unit getDefaultUnit() {
		return defaultUnit;
	}

	public void setDefaultUnit(Unit defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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

	@Override
	public String toString() {
		return "DoneTemplate [id=" + id + ", defaultInitialHour=" + defaultInitialHour + ", defaultInitialMinute="
				+ defaultInitialMinute + ", defaultFinalHour=" + defaultFinalHour + ", defaultFinalMinute="
				+ defaultFinalMinute + ", description=" + description + ", defaultDiscipline=" + defaultDiscipline
				+ ", defaultResult=" + defaultResult + ", defaultUnit=" + defaultUnit + ", channel=" + channel
				+ ", customer=" + customer + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
	
}
