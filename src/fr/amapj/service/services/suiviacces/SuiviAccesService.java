package fr.amapj.service.services.suiviacces;

import java.util.List;

import fr.amapj.service.services.session.SessionManager;


/**
 * Permet d'afficher la liste des personnes connectées
 * 
 *  
 *
 */
public class SuiviAccesService
{
	
	public SuiviAccesService()
	{

	}

	/**
	 * Permet de charger la liste des personnes connectées
	 */
	public List<ConnectedUserDTO> getConnectedUser()
	{
		return SessionManager.getAllConnectedUser();
		
	}	
	
	
	
	
	

		
}
