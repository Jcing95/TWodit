package de.Jcing.window.gui;

public class Table extends ScrollPane {

	
	protected int rows;
	protected int columns;
	
	public Table(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	public Table setRows(int rows) {
		this.rows = rows;
		return this;
	}
	
	public Table setColumns(int columns) {
		this.columns = columns;
		return this;
	}
	
	//TODO: implement Table

}