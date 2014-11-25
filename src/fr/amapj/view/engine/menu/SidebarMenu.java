package fr.amapj.view.engine.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.ChameleonTheme;

@SuppressWarnings("serial")
public class SidebarMenu 
{
	
	
	private CssLayout layout = new CssLayout();

	private List<MenuDescription> allMenus;
	
	private final Navigator nav;
	
	// Element en haut de la liste
	private String firstEntry;
	private NativeButton firstButton;
	
	
	// Correspondance entre les boutons et les vues
	private Map<String, NativeButton> viewNameToMenuButton = new HashMap<String, NativeButton>();
	
	
	public SidebarMenu(List<MenuDescription> allMenus,final Navigator nav)
	{
		this.allMenus = allMenus;
		this.nav = nav;
	}
	
	
	public CssLayout constructMenu()
	{
		layout.setStyleName(ChameleonTheme.COMPOUND_LAYOUT_SIDEBAR_MENU);
		
		boolean first = true;
		
		// Construction du menu
		for (MenuDescription menuDescription : allMenus)
		{
			final String view = menuDescription.getMenuName().name().toLowerCase();
			final String title = menuDescription.getMenuName().getTitle();
			
			
			if (menuDescription.getCategorie()!=null)
			{
				Label l = new Label(menuDescription.getCategorie());
				l.addStyleName("big");
				l.setHeight(null);
				l.setWidth("100%");
				layout.addComponent(l);
			}
			
			
			
			NativeButton b = new NativeButton(title);
			// TODO : faire les icones b.setIcon ...
			addButton(b,view);
			viewNameToMenuButton.put("/"+view, b);
			
			if (first)
			{
				first = false;
				firstButton = b;
				firstEntry = view;
			}
			
		}
		
		// Gestion de l'url
		String f = Page.getCurrent().getUriFragment();
		if (f != null && f.startsWith("!"))
		{
			f = f.substring(1);
		}
		if (f == null || f.equals("") || f.equals("/"))
		{
			nav.navigateTo("/"+firstEntry);
			setSelected(firstButton);
		} 
		else
		{
			nav.navigateTo(f);
			setSelected(viewNameToMenuButton.get(f));
		}
		
		
		
		return layout;
	}
	
	

	public SidebarMenu addButton(NativeButton b,final String view)
	{
		b.setHeight(null);
		b.setWidth("100%");
		layout.addComponent(b);
		
		
		b.addClickListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				updateButtonStyles();
				event.getButton().addStyleName("selected");
				if (nav.getState().equals("/" + view)==false)
				{
					nav.navigateTo("/" + view);
				}
			}
		});
		return this;
	}

	private void updateButtonStyles()
	{
		for (Iterator<Component> iterator = layout.getComponentIterator(); iterator.hasNext();)
		{
			Component c = iterator.next();
			c.removeStyleName("selected");
		}
	}

	public void setSelected(NativeButton b)
	{
		// b peut etre null dans le cas du error view provider 
		if (b==null)
		{
			return ;
		}
		updateButtonStyles();
		b.addStyleName("selected");
	}
}