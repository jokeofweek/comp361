package java2D;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import comp361.shared.data.CellType;
import comp361.shared.data.Field;

public class Test extends JFrame
{
	private static final int _HEIGHT = 600;
	private static final int _WIDTH = 800;

	public Test()
	{
		initUI();
	}
	
	private void initUI()
	{
		setLayout(new BorderLayout());
		

		
		setTitle("Battleships");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(_WIDTH, _HEIGHT);
		
		Field testField = new Field(_WIDTH, _HEIGHT);
		
		for(int x = 0; x < testField.getCellTypeArray().length; x = x + 2)
		{
			for(int y = 0; y < testField.getCellTypeArray()[x].length; y = y + 2)
			{
				Point point = new Point(x, y);
				testField.setCellType(point, CellType.MINE);
			}
		}
		
		add(new GameFieldPanel(testField), BorderLayout.CENTER);
		
		
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run()
			{
				Test t = new Test();
				t.setVisible(true);
			}
		});
	}
}


