package fr.amapj.view.samples.test005;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.errorpopup.ErrorPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.samples.VaadinTest;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Cette classe permet de tester le fonctionnement de base des combo box
 * 
 *  
 *
 */
public class Test005 implements VaadinTest
{
	public void buildView(VaadinRequest request, UI ui)
	{
		VerticalLayout layout = new VerticalLayout();

		final Searcher box = new Searcher(SearcherList.PRODUCTEUR);

		Button b1 = new Button("GET VALUE", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{

				Object o = box.getValue();
				String content = " o=" + o;
				ErrorPopup.open(content);

			}
		});
		
		
		Button b2 = new Button("GET CONVERTED VALUE", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{

				Object o = box.getConvertedValue();
				String content = " o=" + o;
				ErrorPopup.open(content);

			}
		});
		
	
		
		Button b3 = new Button("SET VALUE 101", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{

				box.setConvertedValue(new Long(101));

			}
		});
		
		
		

		layout.addComponent(box);
		layout.addComponent(b1);
		layout.addComponent(b2);
		layout.addComponent(b3);

		ui.setContent(layout);

	}
}
