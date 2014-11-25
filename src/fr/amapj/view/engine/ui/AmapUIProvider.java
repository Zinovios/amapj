package fr.amapj.view.engine.ui;

import java.util.logging.Logger;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;



/**
 * Permet de choisir l'UI en fonction du type de l'appareil (mobile ou non)
 */
public class AmapUIProvider extends UIProvider
{
	
	private final static Logger logger = Logger.getLogger(AmapUIProvider.class.getName());

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event)
	{
		return AmapUI.class;
	}
	
	@Override
	public boolean isPreservedOnRefresh(UICreateEvent event) 
	{
		return true;
	}
	
	@Override
	public String getPageTitle(UICreateEvent event) 
	{
		return  AppConfiguration.getConf().getPageTitle();
	}
	
}