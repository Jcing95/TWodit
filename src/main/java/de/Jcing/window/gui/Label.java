package de.Jcing.window.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import de.Jcing.Main;

public class Label extends Component {
	
	public static final Color DEFAULTFONTCOLOR = Color.CYAN;
	
	public static final int DEFAULT_PADDING = 3;
	
	protected Color color = DEFAULTFONTCOLOR;
	
	protected String text;
	
	protected int baseLineY;
	
	
	
	public Label(String text, int x, int y) {
		super(x,y,Main.getWindow().getFontMetrics().stringWidth(text)+DEFAULT_PADDING*2, Main.getWindow().getFontMetrics().getHeight()+DEFAULT_PADDING*2);
		baseLineY = Main.getWindow().getFontMetrics().getAscent()+DEFAULT_PADDING;
		setText(text);
	}
		
	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		g.drawString(text, DEFAULT_PADDING, baseLineY);
	}

	public void setText(String text) {
		this.text = text;
		setWidth(Main.getWindow().getFontMetrics().stringWidth(text)+DEFAULT_PADDING*2);
//		System.out.println(bounds.width);
	}
	
	public String getText() {
		return text;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
