package de.Jcing.geometry;

import de.Jcing.util.Point;

public class Rectangle {
	
	public double x,y;
	public double width, height;
	
	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean collides(Rectangle r) {
		return false; //TODO: implement rectangle collision logic
	}

	public boolean contains(Point point) {
		return point.x > x && point.x < x+width && point.y > y && point.y < y + height;	
	}
	
	public int compare(Rectangle r) {
		boolean w = width > r.width;
		boolean h = height > r.height;
		if(w && h)
			return 1;
		if(!w && !h)
			return -1;
		return 0;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public int getWidth() {
		return (int)width;
	}
	
	public int getHeight() {
		return (int)height;
	}
}
