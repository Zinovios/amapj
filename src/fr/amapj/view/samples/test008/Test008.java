package fr.amapj.view.samples.test008;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.view.samples.VaadinTest;

/**
 * Cette classe permet de tester le fonctionnement de base des panel
 * 
 *  
 *
 */
public class Test008 implements VaadinTest
{
	public void buildView(VaadinRequest request, UI ui)
	{
		Panel panel = new Panel();
		panel.addStyleName(ChameleonTheme.PANEL_BORDERLESS);
		
		VerticalLayout layout = new VerticalLayout(); 
		
		for (int i = 0; i < 100; i++)
		{
			Button b1 = new Button("XXX");
			layout.addComponent(b1);
		}
		
		
		panel.setContent(layout);
		
		

		ui.setContent(panel);

	}
}
