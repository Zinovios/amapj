package fr.amapj.view.engine.collectioneditor;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;


/**
 * Modelisation d'une ligne dans la table
 * 
 */
public class Row
{
	// Attention : ceci correspond à l'itemId dans l'objet Table
	private Object itemId;
	
	private List<AbstractField> fields = new ArrayList<AbstractField>();
	
	
	public Row()
	{
		
	}
	
	public Object[] getColumnTable()
	{
		Object[] row = new Object[fields.size()+1];
		
		// On met toujours une colonne vide pour rendre les lignes de la table selectionnable
		row[0] =  new Label(" ",ContentMode.HTML);
		
		for (int i = 1; i < row.length; i++)
		{
			row[i]= fields.get(i-1);
		}
		
		return row;
		
	}
	
	
	public void addField(AbstractField<?> f)
	{
		fields.add(f);
	}
	
	
	public Object getFieldValue(int columns)
	{
		return fields.get(columns).getConvertedValue();
	}
	
	public void setFieldValue(int columns,Object val)
	{
		fields.get(columns).setConvertedValue(val);
	}
	
	
	public int getNbFields()
	{
		return fields.size();
	}
	
	

	public Object getItemId()
	{
		return itemId;
	}

	public void setItemId(Object itemId)
	{
		this.itemId = itemId;
	}

	


	
	
}
