package com.faap.scheduler.job_application.excel.models.done;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


public class Periodic {

	private Integer id;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate initialDate;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate finalDate;

	private String description;
	
	private Priority priority;
	
	private DailyCategory dailyCategory;
	
	private Periodicity periodicity;
	
	private Weekday weekday;
	
	private boolean enabled;
	
	private Channel channel;
	
	private Customer customer;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(LocalDate initialDate) {
		this.initialDate = initialDate;
	}

	public LocalDate getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(LocalDate finalDate) {
		this.finalDate = finalDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public DailyCategory getDailyCategory() {
		return dailyCategory;
	}

	public void setDailyCategory(DailyCategory dailyCategory) {
		this.dailyCategory = dailyCategory;
	}

	public Periodicity getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(Periodicity periodicity) {
		this.periodicity = periodicity;
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		return "Periodic [id=" + id + ", initialDate=" + initialDate + ", finalDate=" + finalDate + ", description="
				+ description + ", priority=" + priority + ", dailyCategory=" + dailyCategory + ", periodicity="
				+ periodicity + ", weekday=" + weekday + ", enabled=" + enabled + ", channel=" + channel + ", customer="
				+ customer + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
