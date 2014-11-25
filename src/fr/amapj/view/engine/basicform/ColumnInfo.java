package fr.amapj.view.engine.basicform;

public class ColumnInfo
{

	public String propertyId;
	
	public String title;

	public ColumnInfo(String propertyId, String title)
	{
		this.propertyId = propertyId;
		this.title = title;
	}
	
	
	public boolean isNested()
	{
		return propertyId.indexOf('.')!=-1;
	}
	
	
	
	
}
