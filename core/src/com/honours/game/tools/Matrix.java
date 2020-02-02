package com.honours.game.tools;

import java.util.HashMap;
import java.util.Map;

public class Matrix<R, C, E> {
	private Map<R,Map<C,E>> matrix;
	private R currentRow;
	
	public Matrix() {
		matrix = new HashMap<R, Map<C,E>>();
	}
	
	public Matrix(Map<R,Map<C,E>> matrix) {
		this.matrix = matrix;
	}
	
	public void addRow(R key, Map<C,E> row) {
		currentRow = key;
		matrix.put(key, row);
	}
	
	public void newRow(R key) {
		currentRow = key;
		matrix.put(key, new HashMap<C, E>());
	}
	
	public void addElement(C keyColumn, E element) {
		matrix.get(currentRow).put(keyColumn, element);
	}
	
	public void addElement(R keyRow, C keyColumn, E element) {
		matrix.get(keyRow).put(keyColumn, element);
	}
	
	public Map<C, E> getRow(R key) {
		return matrix.get(key);
	}
	
	public E getElement(R keyRow, C keyColumn) {
		return matrix.get(keyRow).get(keyColumn);
	}
	
}
