package com.faap.scheduler.job_application.models.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

public class SheetRow {
	private List<Cell> cellList;
	
	public SheetRow() {
		this.cellList = new ArrayList<>();
	}
	
	public SheetRow(List<Cell> cellList) {
		this.cellList = cellList;
	}

	public List<Cell> getCellList() {
		return cellList;
	}

	public void setCellList(List<Cell> cellList) {
		this.cellList = cellList;
	}

	@Override
	public String toString() {
		return "SheetRow [cellList=" + cellList + "]";
	}
	
	
}
