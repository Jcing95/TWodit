package de.jcing.engine.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class ProgressBar extends Component {

	protected double percentage;
	
	protected Color back = new Color(0,0,0,0.5f);
	protected Color front = new Color(12,200,18);
	
	public ProgressBar(int x, int y, int w, int h) {
		super(x, y, w, h);
		percentage = 0;
	}
	
	public void setPercentage(double d) {
		this.percentage = d;
	}

	@Override
	protected void paint(Graphics2D g) {
		g.setColor(back);
		g.fillRect(0, 0, bounds.getWidth(), bounds.getHeight());
		g.setColor(front);
		g.fillRect(0, 0, (int)(bounds.getWidth()/100.0*percentage), bounds.getHeight());
	}

	public void setFront(Color color) {
		front = color;
	}
	
	public void setBack(Color color) {
		back = color;
	}

}
