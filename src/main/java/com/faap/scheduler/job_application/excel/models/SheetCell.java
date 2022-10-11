package com.faap.scheduler.job_application.excel.models;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;

public class SheetCell {
	private int rowNumber;
	private SheetCellType sheetCellType;
	private int columnIndex;
	private CellStyle cellStyle;
	private CellType cellType;
	private String cellValue;
	private String cellFormula;
	private Cell cell;
	
	public SheetCell(SheetCellType sheetCellType, Cell cell, int rowNumber, String cellValue) {
		super();
		this.setSheetCellType(sheetCellType);
		this.setCell(cell);
		this.setRowNumber(rowNumber);
		this.setColumnIndex(cell.getColumnIndex());
		this.setCellStyle(cell.getCellStyle());
		this.setCellType(cell.getCellType());
		this.setCellValue(cellValue);
		if(cell.getCellType() == CellType.FORMULA) {
			this.setCellFormula(cell.getCellFormula());
		}
		
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public CellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public SheetCellType getSheetCellType() {
		return sheetCellType;
	}

	public void setSheetCellType(SheetCellType sheetCellType) {
		this.sheetCellType = sheetCellType;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	@Override
	public String toString() {
		return "SheetCell [rowNumber=" + rowNumber + ", sheetCellType=" + sheetCellType + ", columnIndex=" + columnIndex
				+ ", cellStyle=" + cellStyle + ", cellType=" + cellType + ", cellValue=" + cellValue + ", cell=" + cell
				+ "]";
	}

	public String getCellFormula() {
		return cellFormula;
	}

	public void setCellFormula(String cellFormula) {
		this.cellFormula = cellFormula;
	}
	
}
