package de.Jcing.window.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.List;

import de.Jcing.Main;
import de.Jcing.window.gui.utillities.StringUtils;

public class TextPane extends Label {
	
	public static final int LINE_PADDING = 3;
	
	protected boolean wrapText;
	protected int textHeight;
	protected List<String> lines;
	protected FontMetrics fm; 
	
	protected Color background;
	
	public TextPane(String text, int x, int y, int w, int h) {
		super(text, x, y);
		bounds.width = w;
		bounds.height = h;
		wrapText = true;
		fm = Main.getWindow().getFontMetrics();
		lines = StringUtils.wrap(text, fm, bounds.getWidth()-DEFAULT_PADDING);
		textHeight = fm.getHeight();		
	}
	
	@Override
	public void paint(Graphics2D g) {
		g.setColor(color);
		for(String s : lines) {
			g.drawString(s, DEFAULT_PADDING, baseLineY);
			g.translate(0, textHeight + LINE_PADDING);
		}
		g.translate(0, -lines.size() * (textHeight + LINE_PADDING));
	}
	
	
	

}
