package comp361.client.ui.statistics;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class StatisticsTableModel extends AbstractTableModel
{
	/**
	 * The column names
	 */
	private String[] aColumnsNames = { "Description", "Value" };
	
	/**
	 * The data
	 */
	private Vector[] aData = { new Vector<String>(), new Vector<Object>() };

	
	@Override
	public int getRowCount() 
	{
		return aData[0].size();
	}

	@Override
	public int getColumnCount() 
	{
		return aColumnsNames.length;
	}

	@Override
	public Object getValueAt(int pRowIndex, int pColumnIndex) 
	{
		return aData[pColumnIndex].get(pRowIndex);
	}
	
	@Override
	public String getColumnName(int pCol)
	{
		return aColumnsNames[pCol];
	}
	
//	@Override
//	public Class getColumnClass(int pC)
//	{
//		return getValueAt(0, pC).getClass();
//	}
	
	/**
	 * @param pDescription the stat description
	 * @param pValue the stat value
	 */
	public void addNewData(String pDescription, Object pValue)
	{
		aData[0].add(pDescription);
		aData[1].add(pValue);
		
		fireTableDataChanged();
	}
	
}
