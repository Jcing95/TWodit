package de.jcing.util;

public abstract class PointMorph extends Point {
	
	public Point p;
	
	public PointMorph(Point p) {
		super(p.x, p.y);
		this.p = p;
	}
	
	public abstract double morphX(double x);
	
	public abstract double morphY(double y);
	
	
	public int getXi() {
		return (int)morphX(p.getXd());
	}
	
	public int getYi() {
		return (int)morphY(p.getYd());
	}
	
	public double getXd() {
		return morphX(p.getXd());
	}
	
	public double getYd() {
		return morphY(p.getYd());
	}
	
	@Override
	public boolean equals(Object o) {
		return p.equals(o);
	}

	@Override
	public int hashCode() {
		return p.hashCode();
	}

	@Override
	public Point clone() {
		return new Point(getXd(), getYd());
	}

	@Override
	public String toString() {
		return p.toString();
	}

	@Deprecated
	public Point invert() {
		return p.invert();
	}
	
	@Deprecated
	public Point translate(Point translation) {
		return p.translate(translation);
	}
	
	@Deprecated
	public double distance(Point point) {
		return p.distance(point);
	}

	
}
