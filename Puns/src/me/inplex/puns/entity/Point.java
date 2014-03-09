package me.inplex.puns.entity;

public class Point {

	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int distanceX(Point p) {
		int i = p.x;
		int max = x > i ? x : i;
		int min = x > i ? i : x;
		return max - min;
	}

	public int distanceY(Point p) {
		int i = p.y;
		int max = y > i ? y : i;
		int min = y > i ? i : y;
		return max - min;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		Point p = (Point) o;
		if (p.x == x && p.y == y)
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return 4851 + 2 * x + 3 * y;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Point [x=").append(x).append(" y=").append(y).append("]").toString();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}