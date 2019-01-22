package de.jcing.util;

public class Point implements Cloneable {

	public double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getXd() {
		return x;
	}

	public double getYd() {
		return y;
	}
	
	public int getXi() {
		return (int) x;
	}

	public int getYi() {
		return (int) y;
	}
	
	@Override
	public boolean equals(Object o) {
		Point p = (Point)o;
		return p.x == x && p.y == y; 
	}

	@Override
	public int hashCode() {
		return (x + "|" + y).hashCode();
	}

	@Override
	public Point clone() {
		return new Point(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + " | " + y + " )";
	}

	public Point invert() {
		return new Point(-x, -y);
	}

	public Point translate(Point translation) {
		return new Point(x + translation.x, y + translation.y);
	}

	public double distance(Point point) {
		return Math.sqrt((x*point.x)*(x*point.x)+(y*point.y)*(y*point.y));
	}

}
