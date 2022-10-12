package com.faap.scheduler.job_application.excel.models;

public class SheetFormula {
	private String formula;
	private String key;
	private SheetFormulaValue sheetFormulaValue;
	
	
	
	public SheetFormula(String formula, String key, SheetFormulaValue sheetFormulaValue) {
		super();
		this.formula = formula;
		this.key = key;
		this.setSheetFormulaValue(sheetFormulaValue);
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public SheetFormulaValue getSheetFormulaValue() {
		return sheetFormulaValue;
	}
	public void setSheetFormulaValue(SheetFormulaValue sheetFormulaValue) {
		this.sheetFormulaValue = sheetFormulaValue;
	}
	
	
	
}
