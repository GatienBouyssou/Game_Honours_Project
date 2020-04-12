package com.honours.elasticsearch.tools;

public class CaseIdQValuePair {
	private float[] qValues;
	private String caseId;

	public CaseIdQValuePair(float[] qValues, String caseId) {
		this.qValues = qValues;
		this.caseId = caseId;
	}

	public float[] getqValues() {
		return qValues;
	}
	
	public float getqValue(int index) {
		return qValues[index];
	}

	public void setqValue(int index, float qValue) {
		this.qValues[index] = qValue;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
}
