package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public enum SheetCellType {
	ID("ID", true, CellType.STRING, false, null, 5), 
	INCIDENCE_DATE("Incidence Date", true, CellType.NUMERIC, true, null, 20),
	EXECUTION_DATE("Execution Date", false, CellType.NUMERIC, true, null, 20),
	ESTIMATED_DATE("Estimated Date", false, CellType.NUMERIC, true, null, 20),
	PRIORITY("Priority", true, CellType.STRING, false, "A1", 10),
	THINGS_TO_DO("Things to do", true, CellType.STRING, false, "UNKNOWN", 40),
	CATEGORY("Category", true, CellType.STRING, false, "Task", 20),
	STATUS("Status", true, CellType.FORMULA, false, null, 20);
	
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	private int columnWidth;
	
	private SheetCellType(String name, boolean required, CellType cellType, 
			boolean date, Object defaultValue, int columnWidth) {
		this.setName(name);
		this.setRequired(required);
		this.setCellType(cellType);
		this.setDate(date);
		this.setDefaultValue(defaultValue);
		this.setColumnWidth(columnWidth);
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

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}
}
