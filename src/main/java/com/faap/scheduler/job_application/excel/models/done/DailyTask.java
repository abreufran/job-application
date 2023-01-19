package com.faap.scheduler.job_application.excel.models.done;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class DailyTask {

	private Integer id;
	
	private LocalDate incidenceDate;
	
	private LocalDate executionDate;
	
	private LocalDate estimatedDate;
	
	private Priority priority;
	
	private String description;
	
	private DailyCategory dailyCategory;
	
	private boolean completed;

	private String observation;
	
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

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DailyCategory getDailyCategory() {
		return dailyCategory;
	}

	public void setDailyCategory(DailyCategory dailyCategory) {
		this.dailyCategory = dailyCategory;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
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
		return "DailyTask [id=" + id + ", incidenceDate=" + incidenceDate + ", executionDate=" + executionDate
				+ ", estimatedDate=" + estimatedDate + ", priority=" + priority + ", description=" + description
				+ ", dailyCategory=" + dailyCategory + ", completed=" + completed + ", observation=" + observation
				+ ", channel=" + channel + ", customer=" + customer + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
	

}
