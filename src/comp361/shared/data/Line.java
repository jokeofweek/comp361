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
	
	public Line(Point tail, Point head)
	{
		this.head = head;
		this.tail = tail;
	}
	
	public Line(Point tail, Direction direction, int length)
	{
		this.tail = tail;
		if(direction == Direction.DOWN)
			this.head = new Point((int)tail.getX(), (int)(tail.getY()+length));
		if(direction == Direction.UP)
			this.head = new Point((int)tail.getX(), (int)(tail.getY()-length));
		if(direction == Direction.LEFT)
			this.head = new Point((int)(tail.getX()-length), (int)tail.getY());
		if(direction == Direction.DOWN)
			this.head = new Point((int)(tail.getX()+length), (int)(tail.getY()+length));
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
	
	/**
	 * Similar to getP1()
	 * @return the tail of the line segment
	 */
	public Point getTail()
	{
		return this.tail;
	}
	
	/**
	 * Similar to getP2()
	 * @return the head of the line segment
	 */
	public Point getHead()
	{
		return this.head;
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
	 * Implementation of Bresenham's algorithm
	 * @return a list of the points lying on the line
	 */
	public List<Point> getPoints()
	{
		ArrayList<Point> points = new ArrayList<Point>();
		double sx, sy, error, x0, x1, y0, y1;
		x0 = tail.getX();
		x1 = head.getX();
		y0 = tail.getY();
		y1 = head.getY();
		double dx = Math.abs(x1-x0);
		double dy = Math.abs(y1-y0);
		if(x0 < x1)
			sx = 1;
		else sx = -1;
		if(y0 < y1)
			sy = 1;
		else sy = -1;
		error = dx-dy;
		while(x1 != x0 || y1 != y0)
		{
			points.add(new Point((int)x0, (int)y0));
			if(2*error > (-dy))
			{
				error -= dy;
				x0 +=sx;
			}
			if(2*error < dx)
			{
				error += dx;
				y0 += sy;
			}
		}
		points.add(head);
		return points;
	}
	
	
	/**
	 * @return the length of the 
	 */
	public int getLength()
	{
		return (int)Math.sqrt(Math.pow(head.x-tail.x, 2)+Math.pow(head.y-tail.y, 2))+1;
	}
}
