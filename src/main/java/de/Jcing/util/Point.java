package de.Jcing.util;

public class Point implements Cloneable {

	public double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
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

}
