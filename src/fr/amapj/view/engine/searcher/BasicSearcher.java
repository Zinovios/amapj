package fr.amapj.view.engine.searcher;

import java.util.List;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.service.services.searcher.SearcherService;


/**
 * Implementation basique du searcher 
 */
public class BasicSearcher implements SearcherDefinition 
{
	private String title;
	private Class<? extends Identifiable> clazz;
	private String propertyIdToDisplay;
	
	
	public BasicSearcher(String title, Class<? extends Identifiable> clazz, String propertyIdToDisplay)
	{
		this.title = title;
		this.clazz = clazz;
		this.propertyIdToDisplay = propertyIdToDisplay;
	}
	
	public BasicSearcher(String title, Class<? extends Identifiable> clazz, Mdm propertyIdToDisplay)
	{
		this(title,clazz,propertyIdToDisplay.prop());
	}
	
	
	public List<Identifiable> getAllElements(Object params)
	{
		return new SearcherService().getAllElements(clazz);
	}
	
	public String getPropertyId()
	{
		return propertyIdToDisplay;
	}
		
	public String toString(Identifiable identifiable)
	{
		return null;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Class getClazz()
	{
		return clazz;
	}

	@Override
	public boolean needParams()
	{
		return false;
	}

}
