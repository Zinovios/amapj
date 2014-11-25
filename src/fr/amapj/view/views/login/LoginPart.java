package fr.amapj.view.views.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.ui.AmapUI;
import fr.amapj.view.engine.ui.AppConfiguration;

public class LoginPart
{
	private PasswordManager passwordManager = new PasswordManager();
	
	public GridLayout loginLayout = new GridLayout(8, 8);
	
	public LoginPart()
	{
		
	}
	
	public void buildLoginView(boolean exit,CssLayout root,final AmapUI ui,String loginFromUrl,String passwordFromUrl)
	{
		if (exit)
		{
			root.removeAllComponents();
		}
		

		loginLayout.removeAllComponents();
		loginLayout.setSizeFull();
		
		root.addComponent(loginLayout);
		
		
		// Bandeau du haut
		String nomAmap = new ParametresService().getParametres().nomAmap;
		String message  = "<h1>"+nomAmap+"</h1>";
		Panel bandeau = new Panel(message);
		bandeau.addStyleName(ChameleonTheme.PANEL_BORDERLESS);
		loginLayout.addComponent(bandeau,0,0,7,0);
		
		
		// Image sur la partie droite (panier)
		Resource res = new ThemeResource("img/panier.png");
		Image image = new Image(null, res);
		image.setSizeFull();
		loginLayout.addComponent(image,1,2,3,5);
		
		// Zone de saisie login/password sur la gauche 
		final VerticalLayout fields = new VerticalLayout();
		fields.setSpacing(true);
		fields.setMargin(true);
		fields.setWidth("100%");

		final TextField username = new TextField("Adresse Email");
		username.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		username.setWidth("100%");
		if (loginFromUrl!=null)
		{
			username.setValue(loginFromUrl);
		}
		fields.addComponent(username);

		final PasswordField password = new PasswordField("Mot de passe");
		password.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		password.setWidth("100%");
		if (passwordFromUrl!=null)
		{
			password.setValue(passwordFromUrl);
		}
		fields.addComponent(password);
		
		if ((loginFromUrl==null) || (loginFromUrl.length()==0))
		{
			username.focus();
		}
		else
		{
			password.focus();
		}

		final Button signin = new Button("S'identifier");
		signin.addStyleName(ChameleonTheme.BUTTON_BIG);
		signin.addStyleName(ChameleonTheme.BUTTON_DEFAULT);
		fields.addComponent(signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
		
		final Button lostPwd = new Button("Mot de passe perdu");
		lostPwd.addStyleName(ChameleonTheme.BUTTON_BIG);
		lostPwd.addStyleName(ChameleonTheme.BUTTON_LINK);
		fields.addComponent(lostPwd);
		fields.setComponentAlignment(lostPwd, Alignment.BOTTOM_LEFT);
		
		
		

		final ShortcutListener enter = new ShortcutListener("Connexion", KeyCode.ENTER, null)
		{
			@Override
			public void handleAction(Object sender, Object target)
			{
				signin.click();
			}
		};

		signin.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				String msg = passwordManager.checkUser(username.getValue(), password.getValue()); 
				if ( msg == null)
				{
					// Si le mot de passe est correct : on passe à la vue principale
					signin.removeShortcutListener(enter);
					ui.buildMainView();
				} 
				else
				{
					
					// Sinon on affiche le probleme
					if (fields.getComponentCount() > 4)
					{
						// Remove the previous error message
						fields.removeComponent(fields.getComponent(4));
					}
					// Add new error message
					Label error = new Label(msg, ContentMode.HTML);
					error.addStyleName(ChameleonTheme.LABEL_ERROR);
					error.addStyleName(ChameleonTheme.LABEL_BIG);
					error.setSizeUndefined();
					fields.addComponent(error);
					username.focus();
				}
			}
		});

		signin.addShortcutListener(enter);
		
		
		lostPwd.addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleLostPwd();
			}
		});

		

		loginLayout.addComponent(fields,5,2,6,5);
		//loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		
		// Si les deux champs ont été remplis on tente une validation automatique
		if ((passwordFromUrl!=null) && (loginFromUrl!=null))
		{
			signin.click();
		}
		
	}

	/**
	 * Gestion de l'appui sur mot de passe perdu
	 */
	protected void handleLostPwd()
	{
		FormPopup.open(new PopupSaisieEmail());
	}
}
