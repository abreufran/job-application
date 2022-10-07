package com.faap.scheduler.job_application.models.job;

import org.apache.poi.ss.usermodel.CellType;

public enum SheetCellType {
	ID("ID", true, CellType.STRING, false), 
	INCIDENCE_DATE("Incidence Date", true, CellType.NUMERIC, true),
	EXECUTION_DATE("Execution Date", false, CellType.NUMERIC, true),
	ESTIMATED_DATE("Estimated Date", false, CellType.NUMERIC, true),
	PRIORITY("Priority", true, CellType.STRING, false),
	THINGS_TO_DO("Things to do", true, CellType.STRING, false),
	CATEGORY("Category", true, CellType.STRING, false),
	STATUS("Status", true, CellType.FORMULA, false);
	
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	
	private SheetCellType(String name, boolean required, CellType cellType, boolean date) {
		this.setName(name);
		this.setRequired(required);
		this.setCellType(cellType);
		this.setDate(date);
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

	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public boolean isDate() {
		return date;
	}

	public void setDate(boolean date) {
		this.date = date;
	}
}
