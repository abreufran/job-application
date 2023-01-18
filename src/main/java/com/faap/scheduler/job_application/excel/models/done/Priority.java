package com.faap.scheduler.job_application.excel.models.done;

import java.time.LocalDateTime;


public class Priority {

	private Integer id;

	private String name;

	private String description;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return "Priority [id=" + id + ", name=" + name + ", description=" + description + ", channel=" + channel
				+ ", customer=" + customer + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
	
}
