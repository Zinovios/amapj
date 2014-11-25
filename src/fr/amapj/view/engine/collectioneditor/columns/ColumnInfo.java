package fr.amapj.view.engine.collectioneditor.columns;

import fr.amapj.view.engine.collectioneditor.FieldType;

public class ColumnInfo
{

	public String propertyId;
	
	public String title;
	
	public FieldType fieldType;
	
	public Object defaultValue;

	public ColumnInfo(String propertyId, String title,FieldType fieldType,Object defaultValue)
	{
		this.propertyId = propertyId;
		this.title = title;
		this.fieldType = fieldType;
		this.defaultValue = defaultValue;
	}
	
	
	
	
	
}
