package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public enum PeriodicTaskColumnType {
	ID("ID", true, CellType.STRING, false, null, 5, 0, null), 
	INITIAL_DATE("Initial Date", true, CellType.NUMERIC, true, null, 15, 1, null),
	FINAL_DATE("Final Date", false, CellType.NUMERIC, true, null, 15, 2, null),
	TASK("Task", true, CellType.STRING, false, "UNKNOWN", 40, 3, null),
	PRIORITY("Priority", true, CellType.STRING, false, "A1", 10, 4, null),
	CATEGORY("Category", true, CellType.STRING, false, "Task", 20, 5, null),
	PERIODICITY("Periodicity", true, CellType.STRING, false, "Every week", 20, 6, null),
	WEEKDAY("Weekday", true, CellType.STRING, false, "Monday", 20, 7, null),
	STATUS("Status", true, CellType.FORMULA, false, null, 15, 8, 
			new FormulaWrapper("IF(ISBLANK(C_rowNumber_),\"ACTIVE\",\"INACTIVE\")", "_rowNumber_", FormulaValueWrapper.ROW_NUMBER));
	
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	private int columnWidth;
	private int columnIndex;
	private FormulaWrapper formulaWrapper;
	
	private PeriodicTaskColumnType(String name, boolean required, CellType cellType, 
			boolean date, Object defaultValue, int columnWidth, int columnIndex,
			FormulaWrapper formulaWrapper) {
		this.setName(name);
		this.setRequired(required);
		this.setCellType(cellType);
		this.setDate(date);
		this.setDefaultValue(defaultValue);
		this.setColumnWidth(columnWidth);
		this.setColumnIndex(columnIndex);
		this.setSheetFormula(formulaWrapper);
		
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

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public FormulaWrapper getSheetFormula() {
		return formulaWrapper;
	}

	public void setSheetFormula(FormulaWrapper formulaWrapper) {
		this.formulaWrapper = formulaWrapper;
	}
}
