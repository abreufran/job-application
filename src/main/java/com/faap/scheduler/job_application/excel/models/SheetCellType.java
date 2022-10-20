package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.CellType;

public class SheetCellType {
	private String name;
	private boolean required;
	private CellType cellType;
	private boolean date;
	private Object defaultValue;
	private int columnWidth;
	private int columnIndex;
	private SheetFormula sheetFormula;
	
	
	public SheetCellType(ThingToDoColumnType thingToDoColumnType) {
		this.name = thingToDoColumnType.getName();
		this.required = thingToDoColumnType.isRequired();
		this.cellType = thingToDoColumnType.getCellType();
		this.date = thingToDoColumnType.isDate();
		this.defaultValue = thingToDoColumnType.getDefaultValue();
		this.columnWidth = thingToDoColumnType.getColumnWidth();
		this.columnIndex = thingToDoColumnType.getColumnIndex();
		this.sheetFormula = thingToDoColumnType.getSheetFormula();
	}
	
	public SheetCellType(PeriodicTaskColumnType periodicTask) {
		this.name = periodicTask.getName();
		this.required = periodicTask.isRequired();
		this.cellType = periodicTask.getCellType();
		this.date = periodicTask.isDate();
		this.defaultValue = periodicTask.getDefaultValue();
		this.columnWidth = periodicTask.getColumnWidth();
		this.columnIndex = periodicTask.getColumnIndex();
		this.sheetFormula = periodicTask.getSheetFormula();
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
	public SheetFormula getSheetFormula() {
		return sheetFormula;
	}
	public void setSheetFormula(SheetFormula sheetFormula) {
		this.sheetFormula = sheetFormula;
	}
	
	
}
