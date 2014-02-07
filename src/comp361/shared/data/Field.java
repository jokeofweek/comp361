package comp361.shared.data;

/**
 * This class represents a game field.
 */
public class Field {

	private CellType[][] cells;
	private boolean[][] bombPresent;
	
	public Field(int width, int height) {
		// Instantiate every cell to water with no bomb
		cells = new CellType[width][height];
		bombPresent = new boolean[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = CellType.WATER;
			}
		}
	}
	
	public CellType getCellType(int x, int y) {
		return cells[x][y];
	}
	
	public void setCellType(int x, int y, CellType cellType) {
		cells[x][y] = cellType;
	}
	
	public boolean isBombPresent(int x, int y) {
		return bombPresent[x][y];
	}
	
	public void setBombPresent(int x, int y, boolean isBombPresent) {
		bombPresent[x][y] = isBombPresent;
	}
	
}
