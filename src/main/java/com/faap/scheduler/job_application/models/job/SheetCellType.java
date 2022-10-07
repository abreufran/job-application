package com.faap.scheduler.job_application.models.job;

public enum SheetCellType {
	ID("ID", true), 
	INCIDENCE_DATE("Incidence Date", true),
	EXECUTION_DATE("Execution Date", false),
	ESTIMATED_DATE("Estimated Date", false),
	PRIORITY("Priority", true),
	THINGS_TO_DO("Things to do", true),
	CATEGORY("Category", true),
	STATUS("Status", true);
	
	private String name;
	private boolean required;
	
	private SheetCellType(String name, boolean required) {
		this.setName(name);
		this.setRequired(required);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	private void setRequired(boolean required) {
		this.required = required;
	}
}
