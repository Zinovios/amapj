package fr.amapj.view.samples;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;


/**
 * Toutes les classes de test des fonctionnalités basiques de Vaddin doivent implementer 
 * cette interface
 * 
 *
 */
public interface VaadinTest 
{
	
	public void buildView(VaadinRequest request,UI ui);
}