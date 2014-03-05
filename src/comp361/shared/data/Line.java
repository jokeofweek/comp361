package comp361.shared.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Line extends Line2D 
{
	private Point head;
	private Point tail;
	
	public Line(Point head, Point tail)
	{
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(tail.getX(), tail.getY(), 
				head.getX()-tail.getX(), head.getY()-tail.getY());
	}

	@Override
	public Point2D getP1() {
		return new Point2D.Double(tail.getX(), tail.getY());
	}

	@Override
	public Point2D getP2() {
		return new Point2D.Double(head.getX(), head.getY());
	}

	@Override
	public double getX1() {
		return tail.getX();
	}

	@Override
	public double getX2() {
		return head.getX();
	}

	@Override
	public double getY1() {
		return tail.getY();
	}

	@Override
	public double getY2() {
		return head.getY();
	}

	@Override
	public void setLine(double x1, double y1, double x2, double y2) {
		this.tail = new Point((int)x1, (int)y1);
		this.head = new Point((int)x2, (int)y2);
	}
	
	@Override
	public boolean contains(Point2D p) 
	{
		return this.contains(p.getX(), p.getY());
	}
	
	@Override
	public boolean contains(double x, double y)
	{
		return getPoints().contains(new Point((int)x, (int)y));
	}
	
	/**
	 * @return a list of the points lying on the line
	 */
	public List<Point> getPoints()
	{
		ArrayList<Point> points = new ArrayList<Point>();
		//TODO: implement Bresenham's algorithm
		return points;
	}

}
