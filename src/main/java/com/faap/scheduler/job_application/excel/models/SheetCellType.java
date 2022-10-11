package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public enum SheetCellType {
	ID("ID", true, CellType.STRING, false, null), 
	INCIDENCE_DATE("Incidence Date", true, CellType.NUMERIC, true, null),
	EXECUTION_DATE("Execution Date", false, CellType.NUMERIC, true, null),
	ESTIMATED_DATE("Estimated Date", false, CellType.NUMERIC, true, null),
	PRIORITY("Priority", true, CellType.STRING, false, "A1"),
	THINGS_TO_DO("Things to do", true, CellType.STRING, false, "UNKNOWN"),
	CATEGORY("Category", true, CellType.STRING, false, "Task"),
	STATUS("Status", true, CellType.FORMULA, false, null);
	
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	
	private SheetCellType(String name, boolean required, CellType cellType, boolean date, Object defaultValue) {
		this.setName(name);
		this.setRequired(required);
		this.setCellType(cellType);
		this.setDate(date);
		this.setDefaultValue(defaultValue);
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

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}
