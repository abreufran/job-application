package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public class CellTypeWrapper {
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	private int columnWidth;
	private int columnIndex;
	private FormulaWrapper formulaWrapper;
	
	public CellTypeWrapper(DoneColumnType columnType) {
		this.name = columnType.getName();
		this.required = columnType.isRequired();
		this.cellType = columnType.getCellType();
		this.date = columnType.isDate();
		this.defaultValue = columnType.getDefaultValue();
		this.columnWidth = columnType.getColumnWidth();
		this.columnIndex = columnType.getColumnIndex();
		this.formulaWrapper = columnType.getSheetFormula();
	}
	
	public CellTypeWrapper(ThingToDoColumnType columnType) {
		this.name = columnType.getName();
		this.required = columnType.isRequired();
		this.cellType = columnType.getCellType();
		this.date = columnType.isDate();
		this.defaultValue = columnType.getDefaultValue();
		this.columnWidth = columnType.getColumnWidth();
		this.columnIndex = columnType.getColumnIndex();
		this.formulaWrapper = columnType.getSheetFormula();
	}
	
	public CellTypeWrapper(PeriodicTaskColumnType columnType) {
		this.name = columnType.getName();
		this.required = columnType.isRequired();
		this.cellType = columnType.getCellType();
		this.date = columnType.isDate();
		this.defaultValue = columnType.getDefaultValue();
		this.columnWidth = columnType.getColumnWidth();
		this.columnIndex = columnType.getColumnIndex();
		this.formulaWrapper = columnType.getSheetFormula();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
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
