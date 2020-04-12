package com.honours.elasticsearch.tools;

import com.badlogic.gdx.utils.Array;

public class ListCaseIdQValuePair {
	Array<CaseIdQValuePair> array;
	
	public ListCaseIdQValuePair(int sizeList) {
		array = new Array<CaseIdQValuePair>(sizeList);
	}
	
	public ListCaseIdQValuePair(Array<CaseIdQValuePair> array) {
		this.array = array;
	}

	public Array<CaseIdQValuePair> getArray() {
		return array;
	}
	
	public CaseIdQValuePair get(int index) {
		return array.get(index);
	}

	public void setArray(Array<CaseIdQValuePair> array) {
		this.array = array;
	}
	
	public void add(CaseIdQValuePair value) {
		array.add(value);
	}
	
	public void setPairAt(int index, CaseIdQValuePair pair) {
		array.set(index, pair);
	}

	public int size() {
		return array.size;
	}

	public void pull() {
		array.removeIndex(0);
		
	}
	
	
}
