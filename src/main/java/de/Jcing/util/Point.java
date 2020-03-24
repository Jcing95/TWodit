package de.jcing.util;

@Deprecated
public class Point implements Cloneable {

	protected double x, y;

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

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		Point p = (Point) o;
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
		return Math.sqrt((getXd() - point.getXd()) * (getXd() - point.getXd()) + (getYd() - point.getYd()) * (getYd() - point.getYd()));
	}

	public double yDist(Point p) {
		return Math.abs(p.getYd() - getYd());
	}

	public double xDist(Point p) {
		return Math.abs(p.getXd() - getXd());
	}

}
