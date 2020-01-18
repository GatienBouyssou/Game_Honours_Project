package com.honours.game.scenes.ui;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableCreator extends Table {
	
	private Table table;
	
	public TableCreator() {
		table = new Table();
	}
	
	public TableCreator(int align, boolean doFillParent) {
		table = new Table();
		table.align(align);
		table.setFillParent(doFillParent);
	}
	
	public TableCreator(int align, float width, float height) {
		table = new Table();
		table.align(align);
		table.setWidth(width);
		table.setHeight(height);
	}
	
	public void setTableConfiguration(int align, boolean doFillParent) {
		table = new Table();
		table.align(align);
		table.setFillParent(doFillParent);
	}
	
	public void setTableConfiguration(int align, float width, float height) {
		table = new Table();
		table.align(align);
		table.setWidth(width);
		table.setHeight(height);
	}
	
	public void createRow(List<String> listContentCell) {
		for (String content : listContentCell) {
			table.add(LabelCreator.createLabel(content)).expandX();
		}
		table.row();
	}
	
	public <T> void createRowWithCell(List<T> listOfCells) {
		for (T cell : listOfCells) {
			table.add((Actor)cell).expandX();
		}
		table.row();
	}
	
	public static Table createCellTableWithLabels(int align, List<Label> contents) {
		Table table = new Table();
		table.align(align);
		for (Label label : contents) {
			table.add(label).expandX();
		}
		return table;
	}
	
	public static Table createCellTable(int align, List<String> contents) {
		Table table = new Table();
		table.align(align);
		for (String content : contents) {
			table.add(LabelCreator.createLabel(content)).expandX();
		}
		return table;
	}
	
	public void clear() {
		table = new Table();
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	
	
	
}
