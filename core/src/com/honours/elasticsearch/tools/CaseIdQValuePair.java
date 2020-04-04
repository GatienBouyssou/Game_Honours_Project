package com.honours.elasticsearch.tools;

public class CaseIdQValuePair {
	private float qValue;
	private String caseId;

	public CaseIdQValuePair(float qValue, String caseId) {
		this.qValue = qValue;
		this.caseId = caseId;
	}

	public float getqValue() {
		return qValue;
	}

	public void setqValue(float qValue) {
		this.qValue = qValue;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
}
