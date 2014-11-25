package fr.amapj.service.services.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.view.engine.menu.RoleList;
import fr.amapj.view.views.common.contratselector.ContratSelectorSessionInfo;


/**
 * Parametres attachés à une session, après le login de l'utilisateur 
 * 
 * Attention : une session est ici dans le sens de une UI
 * 
 * Il est tout a fait possible d'avoir deux fenetres differentes dans le meme navigateur
 * et d'etre logué sous deux noms differents
 *
 */
public class SessionParameters
{
	
	public SessionParameters()
	{
		
	}
	public Long userId;
	
	// Liste des rôles de la session
	public List<RoleList> userRole;
	
	public String userNom;
	
	public String userPrenom;
	
	public String userEmail;
	
	public Date dateConnexion;
	
	public Long logId;
	
	public ContratSelectorSessionInfo contratSelectorSessionInfo = new ContratSelectorSessionInfo();
	
}
