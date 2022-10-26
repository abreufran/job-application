package com.faap.scheduler.job_application.excel.models;

public class FormulaWrapper {
	private String formula;
	private String key;
	private FormulaValueWrapper formulaValueWrapper;
	
	
	
	public FormulaWrapper(String formula, String key, FormulaValueWrapper formulaValueWrapper) {
		super();
		this.formula = formula;
		this.key = key;
		this.setSheetFormulaValue(formulaValueWrapper);
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
	public FormulaValueWrapper getSheetFormulaValue() {
		return formulaValueWrapper;
	}
	public void setSheetFormulaValue(FormulaValueWrapper formulaValueWrapper) {
		this.formulaValueWrapper = formulaValueWrapper;
	}
	
	
	
}
