package com.faap.scheduler.job_application.excel.dtos;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookResponse {
	private XSSFWorkbook myWorkBook;
	private boolean changed;
	private boolean success;
	
	
	
	public WorkbookResponse(XSSFWorkbook myWorkBook, boolean changed, boolean success) {
		super();
		this.myWorkBook = myWorkBook;
		this.setChanged(changed);
		this.setSuccess(success);
	}
	public XSSFWorkbook getMyWorkBook() {
		return myWorkBook;
	}
	public void setMyWorkBook(XSSFWorkbook myWorkBook) {
		this.myWorkBook = myWorkBook;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}


	
	
}
