package com.faap.scheduler.job_application.excel.models.done;

public class DisciplineDto {
	private Integer id;
	
	private String name;
	
	private String description;
	
	private String channel;
	
	private Integer customerId;

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
		return "DisciplineDto [id=" + id + ", name=" + name + ", description=" + description + ", channel=" + channel
				+ ", customerId=" + customerId + "]";
	}
	
	
}
