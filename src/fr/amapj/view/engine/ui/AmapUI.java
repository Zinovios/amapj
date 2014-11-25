package fr.amapj.view.engine.ui;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionManager.BroadcastListener;
import fr.amapj.view.engine.menu.MenuPart;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.samples.VaadinTest;
import fr.amapj.view.views.login.LoginPart;

/**
 * Classe de démarrage de l'application complete,
 * permettant de swicther entre l'écran de login et la 
 * page normale de l'application
 * 
 */
@Theme("amapj")
@Title("AMAP")
public class AmapUI extends UI implements BroadcastListener
{
	// Layout top level de l'application
	// Contient soit l'écran de login, soit un écran composé d'un menu et d'une
	// zone de travail
	CssLayout root = new CssLayout();

	// Gestion de la page application (écran composé d'un menu et d'une zone de
	// travail)
	private MenuPart menuPart = new MenuPart();

	// Gestion de la page de login
	private LoginPart loginPart = new LoginPart();

	private final static Logger logger = Logger.getLogger(AmapUI.class.getName());

	@Override
	protected void init(VaadinRequest request)
	{
		logger.info("Demarrage d'une nouvelle session:" + getSession().getCsrfToken());
		
		// Register broadcast listener
		SessionManager.register(this);
		
		//
		setLocale(Locale.FRANCE);
	
		// Récupération du nom de la base et vérification de celui ci 
		String dbName = getDbName(request);
		boolean validDbName = DbUtil.checkBbName(dbName);
		if (validDbName==false)
		{
			getPage().setLocation("http://amapj.fr/amap-partenaires.html");
		    return;
		}
		
		
		// Création du contexte et enregistrement du nom de la base
		SessionManager.initSessionData(dbName);
		
	
		// Mode test
		String testClass = getTestClass();
		if (testClass != null)
		{
			invokeTestClass(testClass, request);
			return;
		}

		// Mode normal
		setContent(root);
		root.setSizeFull();

		// Unfortunate to use an actual widget here, but since CSS generated
		// elements can't be transitioned yet, we must
		Label bg = new Label();
		bg.setSizeUndefined();
		root.addComponent(bg);

		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String resetPasswordSalt = request.getParameter("resetPassword");
		if (resetPasswordSalt!=null)
		{
			saisieNewPassword(resetPasswordSalt);
		}

		buildLoginView(false, username,password);

	}
	
	/**
	 * Calcul du nom de la base de données à partir de l'url saisie
	 * @param request
	 * @return
	 */
	public String getDbName(VaadinRequest request)
	{
		String dbName = request.getPathInfo();
		if ((dbName==null) || (dbName.length()<=1))	
		{
			dbName = "amap1";
		}
		else
		{
			dbName = dbName.substring(1);
			// On supprime les eventuels complements dans l'url
			if (dbName.indexOf('/')>0)
			{
				dbName = dbName.substring(0, dbName.indexOf('/'));
			}
		}
		return dbName;
	}
	

	private void saisieNewPassword(String resetPasswordSalt)
	{
		FormPopup.open(new PopupSaisieNewPassword(resetPasswordSalt));
		
	}

	@Override
	public void detach()
	{
		if (SessionManager.canDisconnect()==true)
		{
			new PasswordManager().disconnect();
			
		}
		SessionManager.unregister(this);
		super.detach();
	}

	@Override
	public void receiveBroadcast(final String message)
	{
		access(new Runnable()
		{
			@Override
			public void run()
			{
				Notification n = new Notification("Message recu", message, Type.WARNING_MESSAGE);
				n.show(getPage());
			}
		});
	}

	public void buildLoginView(boolean exit, String username,String password)
	{

		loginPart.buildLoginView(exit, root, this, username,password);
	}

	public void buildMainView()
	{
		menuPart.buildMainView(this, loginPart.loginLayout, root);
	}

	/*
	 * Partie relative aux tests
	 */

	private String getTestClass()
	{
		String nb = AppConfiguration.getConf().getTestMode();
		if ((nb == null) || (nb.length() == 0))
		{
			return null;
		}
		return "fr.amap.view.samples.test" + nb + ".Test" + nb;
	}

	private void invokeTestClass(String testClass, VaadinRequest request)
	{
		try
		{
			VaadinTest vaadinTest = (VaadinTest) Class.forName(testClass).newInstance();
			vaadinTest.buildView(request, this);
		}
		catch (Exception e)
		{
			logger.log(Level.WARNING, "Impossible d'invoquer la classe " + testClass, e);
		}

	}

}
