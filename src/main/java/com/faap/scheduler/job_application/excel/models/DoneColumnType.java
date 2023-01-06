package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public enum DoneColumnType {
	ID("ID", true, CellType.STRING, false, null, 5, 0, null), 
	DATE("Date", true, CellType.NUMERIC, true, null, 20, 1, null),
	INITIAL_HOUR("Hour", true, CellType.NUMERIC, false, null, 8, 2, null),
	INITIAL_MINUTE("Minutes", true, CellType.NUMERIC, false, null, 8, 3, null),
	FINAL_HOUR("Hour", true, CellType.NUMERIC, false, null, 8, 4, null),
	FINAL_MINUTE("Minutes", true, CellType.NUMERIC, false, null, 8, 5, null),
	DESCRIPTION("Task", true, CellType.STRING, false, null, 40, 6, null),
	DISCIPLINE("Discipline", true, CellType.STRING, false, null, 20, 7, null),
	RESULT("Results", false, CellType.STRING, false, null, 8, 8, null),
	UNIT("Unit", false, CellType.STRING, false, null, 8, 9, null);
	
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	private int columnWidth;
	private int columnIndex;
	private FormulaWrapper formulaWrapper;
	
	private DoneColumnType(String name, boolean required, CellType cellType, 
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
