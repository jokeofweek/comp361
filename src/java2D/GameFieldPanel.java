package java2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import comp361.client.ui.SwagFactory;
import comp361.shared.Constants;
import comp361.shared.data.CellType;
import comp361.shared.data.Field;
import comp361.shared.data.Ship;

public class GameFieldPanel extends JPanel {

	Field aField;
	Map<Point, Rectangle2D> graphicField;

	GameFieldPanel(Field pField) {
		SwagFactory.style(this);
		aField = pField;
		graphicField = new HashMap<Point, Rectangle2D>();
		populateGraphicField();

		// Set dimensions
		Dimension d = new Dimension(Constants.TILE_SIZE * Constants.MAP_WIDTH,
				Constants.TILE_SIZE * Constants.MAP_HEIGHT);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(d);
	}

	private void populateGraphicField() {
		// int x = 0;
		// int y = 0;
		for (int x = 0; x < aField.getCellTypeArray().length; x++) {
			for (int y = 0; y < aField.getCellTypeArray()[x].length; y++) {
				Point point = new Point(x, y);
				graphicField.put(point, new Rectangle2D.Double(x
						* Constants.TILE_SIZE, y * Constants.TILE_SIZE,
						Constants.TILE_SIZE, Constants.TILE_SIZE));
			}
		}
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		drawRectangles(g2d);
	}

	private void drawShips(Graphics g, ArrayList<Ship> ships) {
		for (Ship ship : ships) {

		}
	}

	public void drawRectangles(Graphics2D g) {
		for (Point point : graphicField.keySet()) {
			// System.out.println(graphicField.keySet().size());
			// System.err.println(point.getX());
			// System.err.println(point.getY());

			// System.out.println(aField.getCellTypeArray().length);
			CellType type = aField.getCellTypeArray()[(int) point.getX()][(int) point
					.getY()];

			if (type == CellType.BASE) {
				g.setColor(Color.GREEN);
				g.draw(graphicField.get(point));
				g.fill(graphicField.get(point));
			}

			else if (type == CellType.MINE) {
				g.setColor(Color.RED);
				g.draw(graphicField.get(point));
				g.fill(graphicField.get(point));
			}

			else if (type == CellType.REEF) {
				g.setColor(Color.BLACK);
				g.draw(graphicField.get(point));
				g.fill(graphicField.get(point));
			}

			else {
				g.setColor(Color.BLUE);
				g.draw(graphicField.get(point));
				g.fill(graphicField.get(point));
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

	}
}