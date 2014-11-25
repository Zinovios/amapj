package fr.amapj.view.engine.collectioneditor.columns;

import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.engine.searcher.SearcherDefinition;

public class SearcherColumn extends ColumnInfo
{

	public SearcherDefinition searcher;
	public Object params;
	public Searcher linkedSearcher;
	

	public SearcherColumn(String propertyId, String title, FieldType fieldType, Object defaultValue, SearcherDefinition searcher,Object params)
	{
		super(propertyId, title, fieldType, defaultValue);
		this.searcher = searcher;
		this.params = params;
	}
	
	
	public SearcherColumn(String propertyId, String title, FieldType fieldType, Object defaultValue, SearcherDefinition searcher,Searcher linkedSearcher)
	{
		super(propertyId, title, fieldType, defaultValue);
		this.searcher = searcher;
		this.linkedSearcher = linkedSearcher;
	}

}
