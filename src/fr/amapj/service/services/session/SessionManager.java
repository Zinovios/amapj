package fr.amapj.service.services.session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.vaadin.server.WebBrowser;
import com.vaadin.ui.UI;

import fr.amapj.service.services.suiviacces.ConnectedUserDTO;
import fr.amapj.view.engine.menu.RoleList;

/**
 * Contient la liste de toutes les sessions (dans le sens UI)
 *  
 *
 */
public class SessionManager
{

	private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<BroadcastListener>();

	public static void register(BroadcastListener listener)
	{
		listeners.add(listener);
	}

	public static void unregister(BroadcastListener listener)
	{
		listeners.remove(listener);
	}

	public static void broadcast(final String message)
	{
		for (BroadcastListener listener : listeners)
		{
			listener.receiveBroadcast(message);
		}
	}

	public static List<ConnectedUserDTO> getAllConnectedUser()
	{
		List<ConnectedUserDTO> us = new ArrayList<ConnectedUserDTO>();
		for (BroadcastListener listener : listeners)
		{
			ConnectedUserDTO u = new ConnectedUserDTO();
			UI ui = (UI) listener;
			
			u.agent = getAgentName(ui);
			
			if (ui.getData()!=null)
			{
				u.dbName = ((SessionData) ui.getData()).dbName;
				
				SessionParameters params = ((SessionData) ui.getData()).sessionParameters;
				
				if (params!=null)
				{
					u.nom = params.userNom;
					u.prenom = params.userPrenom;
					u.email = params.userEmail;
					u.date = params.dateConnexion;
				}
				else
				{
					// Concerne les personnes sur la page de login
					u.nom = "--";
					u.prenom = "--";
				}
			}
			
			us.add(u);
		}
		return us;
	}
	
	
	/**
	 * Permet d'obtenir le nom du navigateur en clair
	 */
	public static String getAgentName(UI ui)
	{
		if (ui.getSession()==null)
		{
			return "Session null";
		}
		
		
		WebBrowser browser = ui.getPage().getWebBrowser();
		if (browser.isChrome())
		{
			return "Chrome "+browser.getBrowserMajorVersion();
		}
		else if (browser.isAndroid())
		{
			return "Android "+browser.getBrowserMajorVersion();
		} 
		else if (browser.isFirefox())
		{
			return "Firefox "+browser.getBrowserMajorVersion();
		}
		else if (browser.isIE())
		{
			return "IE "+browser.getBrowserMajorVersion();
		}
		else if (browser.isOpera())
		{
			return "Opera "+browser.getBrowserMajorVersion();
		}
		else if (browser.isSafari())
		{
			return "Safari "+browser.getBrowserMajorVersion();
		}
		else
		{
			return browser.getBrowserApplication();
		}
	}



	public interface BroadcastListener
	{
		public void receiveBroadcast(String message);
	}
	
	
	
	static public SessionParameters getSessionParameters()
	{
		return getSessionData().sessionParameters;
	}

	static public void setSessionParameters(SessionParameters param)
	{
		getSessionData().sessionParameters = param;
	}
	
	static public void initSessionData(String dbName)
	{
		SessionData data = new SessionData();
		data.dbName = dbName;
		UI.getCurrent().setData(data);
	}
	
	static private SessionData getSessionData()
	{
		return ((SessionData) UI.getCurrent().getData());
	}
	
	static public boolean canDisconnect()
	{
		return getSessionData()!=null && getSessionParameters()!=null;
	}
	
	
	
	
	
	static public Long getUserId()
	{
		return getSessionParameters().userId;
	}
	
	static public List<RoleList> getUserRoles()
	{
		return getSessionParameters().userRole;
	}
	
	
	static public String getDbName()
	{
		return getSessionData().dbName;
	}
	
	
	
}