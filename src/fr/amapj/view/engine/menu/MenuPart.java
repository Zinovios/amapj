package fr.amapj.view.engine.menu;

import java.util.List;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.view.engine.ui.AmapUI;


/**
 * Classe de gestion du menu
 */

public class MenuPart
{
	CssLayout content = new CssLayout();
	
	AmapUI ui;
	
	private  Navigator nav;
	
	
	public MenuPart()
	{
	}
	
	public void buildMainView(final AmapUI ui,GridLayout loginLayout,CssLayout root)
	{
		this.ui = ui;
		
		nav = new Navigator(ui, content);

		// Chargement de tous les menus accesibles par l'utilisateur
		// et création du "navigator"
		List<MenuDescription> allMenus = MenuInfo.getInstance().getMenu();
		
		if (allMenus.size()>0)
		{
			MenuDescription first = allMenus.get(0);
			nav.setErrorView(first.getViewClass());
		}
		
		for (MenuDescription mD : allMenus)
		{
			nav.addView("/"+mD.getMenuName().name().toLowerCase(), mD.getViewClass());
		}

		// Suppression de la page de login
		root.removeComponent(loginLayout);

		// Création de la page complete (partie de gauche avec le menu et de droite avec la page aplicative)
		HorizontalSplitPanel page = createPage(allMenus);
		root.addComponent(page);
		
	}

	private HorizontalSplitPanel createPage(List<MenuDescription> allMenus)
	{
		HorizontalSplitPanel layout = new HorizontalSplitPanel();
		layout.setSizeFull();
		
		// Partie de gauche contenant le menu
		layout.addComponent(createLeftPart(allMenus));
		
		// Partie de droite avec le contenu
		layout.addComponent(content);
		content.setSizeFull();
		
		layout.setSplitPosition(15);
		
		return layout;
	}
	
	
	private Component createLeftPart(List<MenuDescription> allMenus)
	{
		VerticalLayout layout = new VerticalLayout();
		
		layout.addStyleName(ChameleonTheme.COMPOUND_LAYOUT_SIDEBAR_MENU);
		layout.setSizeFull();
		
		String nomAmap = new ParametresService().getParametres().nomAmap;
		String message  = "<h2>"+nomAmap+"</h2>";
		
		// Partie haute du menu
		layout.addComponent(new Label(message, ContentMode.HTML));
		layout.addComponent(new Label(""));
		layout.addComponent(new Label(""));
		
		
		
		// Partie mediane du menu
		SidebarMenu sidebarMenu = new SidebarMenu(allMenus,nav);
		CssLayout lay = sidebarMenu.constructMenu(); 
		lay.setSizeUndefined();
		
		
		Panel p = new Panel();
		p.setSizeFull();
		p.setContent(lay);
		p.addStyleName(ChameleonTheme.PANEL_BORDERLESS);
		
		layout.addComponent(p);
		layout.setExpandRatio(p, 5);
		
		// Partie basse du menu
		CssLayout toolbar = createBottomMenu();
		layout.addComponent(toolbar);
		layout.setExpandRatio(toolbar, 1);
		layout.setComponentAlignment(toolbar, Alignment.BOTTOM_CENTER);
		
		
		return layout;
	}

	private CssLayout createBottomMenu()
	{
		CssLayout toolbar = new CssLayout();
        toolbar.setWidth("100%");
        toolbar.setStyleName("toolbar");
        
				
		
		Button exit = new Button();
		exit.setIcon(new ThemeResource("img/power.png"));
		exit.setDescription("Se déconnecter");
		exit.setStyleName("icon-only");

				
		
		toolbar.addComponent(exit);
		exit.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new PasswordManager().disconnect();
				ui.buildLoginView(true,null,null);
			}
		});
		
		SessionParameters p = SessionManager.getSessionParameters();
			
		Button name = new Button(p.userPrenom+" "+p.userNom);
		name.addStyleName(ChameleonTheme.BUTTON_BORDERLESS);
		toolbar.addComponent(name);
			

		return toolbar;
	}
	

}
