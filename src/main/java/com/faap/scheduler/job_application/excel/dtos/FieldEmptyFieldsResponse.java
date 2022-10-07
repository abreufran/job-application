package com.faap.scheduler.job_application.excel.dtos;

public class FieldEmptyFieldsResponse {
	private boolean filled;
	
	

	public FieldEmptyFieldsResponse(boolean filled) {
		super();
		this.filled = filled;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
}
