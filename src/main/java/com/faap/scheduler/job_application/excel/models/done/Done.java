package com.faap.scheduler.job_application.excel.models.done;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Done {

	private Integer id;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate date;
	
	private int initialHour;

	private int initialMinute;
	
	private int finalHour;

	private int finalMinute;
	
	private DoneTemplate doneTemplate;
	
	private Discipline discipline;
	
	private Double result;
	
	private Unit unit;
	
	private String observation;
	
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getInitialHour() {
		return initialHour;
	}

	public void setInitialHour(int initialHour) {
		this.initialHour = initialHour;
	}

	public int getInitialMinute() {
		return initialMinute;
	}

	public void setInitialMinute(int initialMinute) {
		this.initialMinute = initialMinute;
	}

	public int getFinalHour() {
		return finalHour;
	}

	public void setFinalHour(int finalHour) {
		this.finalHour = finalHour;
	}

	public int getFinalMinute() {
		return finalMinute;
	}

	public void setFinalMinute(int finalMinute) {
		this.finalMinute = finalMinute;
	}

	public DoneTemplate getDoneTemplate() {
		return doneTemplate;
	}

	public void setDoneTemplate(DoneTemplate doneTemplate) {
		this.doneTemplate = doneTemplate;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
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
		return "Done [id=" + id + ", date=" + date + ", initialHour=" + initialHour + ", initialMinute=" + initialMinute
				+ ", finalHour=" + finalHour + ", finalMinute=" + finalMinute + ", doneTemplate=" + doneTemplate
				+ ", discipline=" + discipline + ", result=" + result + ", unit=" + unit + ", observation="
				+ observation + ", channel=" + channel + ", customer=" + customer + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	
	
}
