package com.honours.game.scenes.ui;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableCreator {
	
	public static Table setTableConfiguration(int align) {
		Table table = new Table();
		table.align(align);
		table.setFillParent(true);
		return table;
	}
	
	public static Table setTableConfiguration(int align, float width, float height) {
		Table table = new Table();
		table.align(align);
		table.setWidth(width);
		table.setHeight(height);
		return table;
	}
	
	public static void createRow(Table table, List<String> listContentCell) {
		for (String content : listContentCell) {
			table.add(LabelCreator.createLabel(content)).expandX();
		}
		table.row();
	}
	
	public static <T> void createRowWithCell(Table table, List<T> listOfCells) {
		for (T cell : listOfCells) {
			table.add((Actor)cell).expandX();
		}
		table.row();
	}
	
	public static Table createCellTableWithLabels(int align, List<Label> contents) {
		Table table = new Table();
		table.align(align);
		for (Label label : contents) {
			table.add(label).colspan(2);
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

	public static void createCellWithList(Table table, List<Label> element) {
		Stack stack = new Stack();
		for (Label label : element) {
			stack.add(label);
		}
		table.add(stack).expandX();
	}
	
	
}
