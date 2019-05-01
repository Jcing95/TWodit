package de.jcing.geometry;

import org.joml.Vector2f;

public class Rectangle {
	
	public Vector2f pos;
	public Vector2f size;
	
	public Rectangle(Vector2f pos, Vector2f size) {
		this(pos.x,pos.y,size.x,size.y);
	}
	
	
	public Rectangle(float x, float y, float width, float height) {
		pos = new Vector2f(x,y);
		size = new Vector2f(width,height);
	}
	
	public boolean collides(Rectangle r) {
		return false; //TODO: implement rectangle collision logic
	}

	public boolean contains(Vector2f point) {
		return point.x() > pos.x && point.x() < pos.x+size.x && point.y > pos.y && point.y < pos.y + size.y;	
	}
	
	public int compare(Rectangle r) {
		boolean w = size.x > r.getWidth();
		boolean h = size.y > r.getHeight();
		if(w && h)
			return 1;
		if(!w && !h)
			return -1;
		return 0;
	}
	
	@Override
	public Rectangle clone() {
		return new Rectangle(pos,size);
	}
	
	@Override 
	public String toString() {
		return "Rectangle: (" + pos.x + "|" + pos.y + ") - W: " + size.x + ", h: " + size.y;
	} 

	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}

}
